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

/**
 *
 */
package com.epocharch.fawkes.common.context;

import com.epocharch.common.config.PropertiesContainer;
import com.epocharch.common.util.SystemUtil;
import com.epocharch.fawkes.common.constants.Constants;
import com.epocharch.fawkes.common.dto.ClientProfile;
import com.epocharch.fawkes.common.dto.ServiceProfile;
import com.epocharch.fawkes.common.utils.FawkesStringUtils;
import com.epocharch.fawkes.common.utils.FawkesUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton container, use to store global information of application.
 *
 * @author Archer
 */
public class RootContainer {

	private static Logger logger = LoggerFactory.getLogger(RootContainer.class);
	private static RootContainer container = new RootContainer();
	// unique ID in the whole system.
	private static String appId;
	private String appName;
	private String contextPath;
	private String hostIp;
	private Map<String, ServiceProfile> serviceProfileMap = new HashMap<String, ServiceProfile>();
	private Map<String, ClientProfile> clientProfileMap = new HashMap<String, ClientProfile>();
	private Map<String, String> servicePoolNameMap = new HashMap<String, String>();

	public static RootContainer getInstance() {
		PropertiesContainer.getInstance().loadProperties(Constants.CONFIG_FILE_NAME, Constants.NAMESPACE_FAWKES);
		String tmp = FawkesUtil.getProperty(Constants.APP_ID);
		appId = FawkesStringUtils.limitString(tmp, Constants.APPID_MAX_LENGTH);
		return container;
	}

	private RootContainer() {
		super();
		hostIp = SystemUtil.getLocalhostIp();
	}

	public String getAppName() {
		return appName;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		if (StringUtils.isBlank(contextPath)) {
			this.contextPath = contextPath;
		}
	}

	public String getHostIp() {
		return hostIp;
	}

	public void putServiceProfile(String sName, ServiceProfile profile) {
		String key = buildProfileKey(sName, profile.getProfileUUId());
		if (!serviceProfileMap.containsKey(key)) {
			serviceProfileMap.put(key, profile);
		} else {
			throw new RuntimeException(key + " is already existed!!");
		}
	}

	public ServiceProfile getServiceProfile(String sName, String profileUUId) {
		String key = buildProfileKey(sName, profileUUId);
		return serviceProfileMap.get(key);
	}

	public ClientProfile getClientProfile(String sName, String profileUUId) {
		String key = buildProfileKey(sName, profileUUId);
		return clientProfileMap.get(key);
	}

	public void putClientProfile(String sName, ClientProfile profile) {
		String key = buildProfileKey(sName, profile.getProfileUUId());
		if (!clientProfileMap.containsKey(key)) {
			clientProfileMap.put(key, profile);
		} else {
			throw new RuntimeException(key + "is already existed!!");
		}
	}

	public void putServicePoolName(String path, String poolName) {
		if (path != null && poolName != null) {
			this.servicePoolNameMap.put(path, poolName);
		}
	}

	public String getServicePoolName(String path) {
		if (path != null) {
			return this.servicePoolNameMap.get(path);
		} else {
			return null;
		}
	}

	public void clean(){
		serviceProfileMap = new HashMap<String, ServiceProfile>();
		clientProfileMap = new HashMap<String, ClientProfile>();
		servicePoolNameMap = new HashMap<String, String>();
	}

	public static String buildProfileKey(String name,String profileUUId){
		return name+"."+profileUUId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		if(StringUtils.isBlank(this.appId)){
			this.appId = appId;
		}

	}

	public void setAppName(String appName) {
		if(StringUtils.isBlank(this.appName)){
			this.appName = appName;
		}
	}



}
