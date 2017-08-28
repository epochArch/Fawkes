/*
 *
 *  * Copyright 2017 EpochArch.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.epocharch.fawkes.common.zk;

import com.epocharch.common.constants.ClusterUsage;
import com.epocharch.common.zone.zk.ZkZoneContainer;
import com.epocharch.fawkes.common.constants.ZkPathConstants;
import com.epocharch.fawkes.common.dto.BaseProfile;
import com.epocharch.fawkes.common.dto.ServiceProfile;
import com.epocharch.fawkes.common.exception.FawkesException;
import com.epocharch.fawkes.common.exception.InvalidMappingException;
import com.epocharch.fawkes.common.exception.InvalidParamException;
import com.epocharch.fawkes.common.utils.FawkesStringUtils;
import com.epocharch.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ZoneZkUtil {
	private static Logger logger = LoggerFactory.getLogger(ZoneZkUtil.class);

	private static List<String> appPathDict;

	public static String createAppcodeDict(BaseProfile profile, String appcode, ZkClient zoneZk)
			throws InvalidParamException, InvalidMappingException {
		if (profile == null) {
			throw new InvalidParamException(" Service profile must not null!!!");
		}
		if (StringUtils.isBlank(appcode)) {
			appcode = "defaultLaserAppName";
		}
		String filterCode = FawkesStringUtils.replaceSlash(appcode); // defaultLaserAppName
		StringBuilder pathBuilder = new StringBuilder(ZkPathConstants.APP_PATH_DICT);
		String appPath = ZoneZkUtil.createAppPath(profile);
		String fAppPath = FawkesStringUtils.replaceSlash(appPath);
		pathBuilder.append("/").append(fAppPath).append(":").append(filterCode);
		String path = pathBuilder.toString();

		if (path.endsWith("/") == false && zoneZk.exists(path) == false) { // 映射关系不存在  // 前面已经创建
			String contextPath = path.substring(0, path.indexOf(":"));
			// 如果path不存在，但已经存在contextPath说明是脏数据退出并打印异常
			if (zoneZk.exists(contextPath)) {
				logger.error("#### CreateAppcodeDict Path ERROR!! ContextPath Already Existed!! contextPath=" + contextPath + "--- path="
						+ path);
				System.exit(-1);
			} else {

				/// validation of servicePath and poolid relationships
				List<String> appPathDict = getAppPathDict();
				String servicePath = fAppPath;

				int sumOfRelations = 0;
				for (String fullPath : appPathDict) {
					String[] pathAndPoolidMapping = fullPath.split(":");
					if (pathAndPoolidMapping.length == 2) {
						String svrPath = pathAndPoolidMapping[0];
						if (servicePath.equals(svrPath)) {
							sumOfRelations++;
							logger.info("Mapping:" + fullPath + " existed on zk");
						}
					}
				}

				if (sumOfRelations > 0) {
					logger.info("sumOfRelations :" + sumOfRelations);
					logger.error("#### There are 1:n relationships between servicePath and poolId, pls clean the relationships first.");

					System.exit(-1);
					throw new InvalidMappingException(
							"#### There are 1:n relationships between servicePath and poolId, pls clean the relationships first.");
				}
				zoneZk.createPersistent(path, true); // 创建映射关系
			}
		}
		return path;
	}

	public synchronized static List<String> getAppPathDict() {
		if (appPathDict == null) {
			ZkClient zk = ZkZoneContainer.getInstance().getLocalZkClient(ClusterUsage.SOA);
			if(zk!=null){
				appPathDict = zk.getChildren(ZkPathConstants.APP_PATH_DICT);
			}
		}
		return appPathDict;
	}

	public static String generatePath(BaseProfile profile, String subPath, ZkClient zoneZk) throws InvalidParamException {
		String value = "";
		if (profile == null && subPath != null)
			throw new InvalidParamException(" Service profile must not null!!!");
		StringBuilder path = new StringBuilder(profile.getRootPath() == null ? "" : profile.getRootPath());
		path.append("/").append(profile.getDomainName()).append("/").append(profile.getServiceAppName()).append("/").append(subPath);
		value = path.toString();
		createPersistentPathIfNotExit(value, zoneZk);
		return value;
	}

	public static String createRollPath(BaseProfile profile, ZkClient zoneZk) throws InvalidParamException {
		return generatePath(profile, ZkPathConstants.PAHT_ROLL, zoneZk);
	}

	public static String createRefugeePath(BaseProfile profile, ZkClient zoneZk) throws InvalidParamException {
		return generatePath(profile, ZkPathConstants.PAHT_CAMPS + "/" + ZkPathConstants.PAHT_REFUGEE, zoneZk);
	}

	public static String createCampPath(BaseProfile profile, String campName, ZkClient zoneZk) throws InvalidParamException {
		return generatePath(profile, ZkPathConstants.PAHT_CAMPS + "/" + campName, zoneZk);
	}

	public static String createBaseCampPath(BaseProfile profile, ZkClient zoneZk) {
		String value = "";
		try {
			value = generatePath(profile, ZkPathConstants.PAHT_CAMPS, zoneZk);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void createPersistentPathIfNotExit(String path, ZkClient zkClient) {
		if (!path.endsWith("/") && !zkClient.exists(path)) {
			zkClient.createPersistent(path, true);
		}
	}

	public static void createEphemeralPathIfNotExit(String path, Object data, ZkClient zkClient) {
		if (!path.endsWith("/") && !zkClient.exists(path)) {
			zkClient.createEphemeral(path, data);
		}
	}

	public static String createChildPath(ServiceProfile profile) throws InvalidParamException {
		if (profile == null)
			throw new InvalidParamException(" Service profile must not null!!!");
		StringBuilder path = new StringBuilder(profile.getParentPath()).append("/").append(getProcessDesc(profile));
		return path.toString();
	}

	public static String getProcessDesc(ServiceProfile profile) throws InvalidParamException {
		StringBuilder path = new StringBuilder().append(profile.getHostIp()).append(":").append(profile.getPort());
		return path.toString();
	}

	public static String createAppPath(BaseProfile profile) throws InvalidParamException {
		if (profile == null)
			throw new InvalidParamException(" Service profile must not null!!!");
		StringBuilder appPath = new StringBuilder(profile.getRootPath());
		appPath.append("/").append(profile.getDomainName()).append("/").append(profile.getServiceAppName());
		return appPath.toString();
	}

	public static String createParentPath(BaseProfile profile) throws InvalidParamException {
		if (profile == null)
			throw new InvalidParamException(" Service profile must not null!!!");
		StringBuilder path = new StringBuilder(createAppPath(profile));
		path.append("/").append(profile.getServiceName()).append("/").append(profile.getServiceVersion());
		return path.toString();
	}

	public static String assembleProfilePath(String domain, String appName, String servName, String servVersion, String providerHost)
			throws Exception {

		StringBuilder sb = new StringBuilder(ZkPathConstants.BASE_ROOT);
		if (!StringUtils.isBlank(domain)) {
			sb.append("/").append(domain);
		} else {
			throw new Exception("Domain must not null!!!");
		}
		if (!StringUtils.isBlank(appName)) {
			sb.append("/").append(appName);
		} else {
			throw new Exception("appName must not null!!!");
		}
		if (!StringUtils.isBlank(servName)) {
			sb.append("/").append(servName);
		} else {
			throw new Exception("servName must not null!!!");
		}
		if (!StringUtils.isBlank(servVersion)) {
			sb.append("/").append(servVersion);
		} else {
			throw new Exception("servVersion must not null!!!");
		}
		if (!StringUtils.isBlank(providerHost)) {
			sb.append("/").append(providerHost);
		} else {
			throw new Exception("providerHost must not null!!!");
		}
		return sb.toString();
	}

	/**
	 * ZK 数据copy工具(不包含临时节点）
	 *
	 * @param from
	 *            源ZK集群客户端
	 * @param fromPath
	 *            源ZK数据路径
	 * @param to
	 *            目标ZK集群客户端
	 * @param toPath
	 *            目标ZK数据路径（如果为NULL，则和源数据路径一致）
	 */
	public static void doCopy(ZkClient from, String fromPath, ZkClient to, String toPath) {
		if (fromPath != null) {
			List<String> childPaths = from.getChildren(fromPath);
			// 叶子节点做拷贝动作，完成后退出
			if (childPaths == null || childPaths.size() == 0) {
				if (toPath == null) {
					toPath = fromPath;
				}
				Stat fromStat = from.existsWithStat(fromPath);
				// 非临时节点才做拷贝
				if (fromStat != null && fromStat.getEphemeralOwner() == 0) {
					byte[] data = from.readRawData(fromPath, true);
					if (!to.exists(toPath)) {
						to.createPersistent(toPath, true);
						to.writeRawData(toPath, data, -1);
					} else {
						to.writeRawData(toPath, data, -1);
					}
				}
				return;
			}
			// 非叶子节点继续执行递归
			for (String childPath : childPaths) {
				StringBuilder fromChildPath = new StringBuilder(fromPath).append("/").append(childPath);
				if (fromPath.equals("/")) {
					fromChildPath = new StringBuilder(fromPath).append(childPath);
				}
				StringBuilder toChildPath = fromChildPath;
				if (toPath != null) {
					if (toChildPath.equals("/")) {
						toChildPath = new StringBuilder(toChildPath).append(childPath);
					} else {
						toChildPath = new StringBuilder(toPath).append("/").append(childPath);
					}
				}
				doCopy(from, fromChildPath.toString(), to, toChildPath.toString());
			}
		}
	}
}
