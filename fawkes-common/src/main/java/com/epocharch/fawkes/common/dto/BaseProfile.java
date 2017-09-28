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
package com.epocharch.fawkes.common.dto;

import com.epocharch.fawkes.common.constants.Constants;
import com.epocharch.fawkes.common.constants.ProtocolType;
import com.epocharch.fawkes.common.zk.ZkPathConstants;
import com.epocharch.fawkes.common.exception.InvalidParamException;
import com.epocharch.fawkes.common.utils.FawkesUtil;
import com.epocharch.fawkes.common.uuid.MD5;
import com.epocharch.fawkes.common.zk.ZoneZkUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author Archer Jiang
 * 
 */
public class BaseProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6856572567927129370L;
	protected String rootPath = ZkPathConstants.BASE_ROOT;
	protected String domainName = Constants.UNKONW_DOMAIN;
	protected String parentPath;
	protected String serviceAppName = "defaultLaserAppName";
	protected String serviceName = "defaultLaserServiceName";
	protected String serviceVersion = "defaultLaserVersion";
	protected ProtocolType transProtocol = ProtocolType.HTTP;
	protected String profileUUId = "";

	public BaseProfile() {
		super();
	}

	public String getRootPath() {
		return rootPath;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = FawkesUtil.filterString(domainName);
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getServiceAppName() {
		return serviceAppName;
	}

	public void setServiceAppName(String serviceAppName) {
		this.serviceAppName = FawkesUtil.filterString(serviceAppName);
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = FawkesUtil.filterString(serviceName);
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = FawkesUtil.filterString(serviceVersion);
	}

	public ProtocolType getTransProtocol() {
		return transProtocol == null ? ProtocolType.HTTP : transProtocol;
	}

	public void setTransProtocol(ProtocolType transProtocol) {
		this.transProtocol = transProtocol;
	}

	public String getProfileUUId() {
		if (StringUtils.isBlank(profileUUId)) {
			StringBuilder sb = new StringBuilder();
			if (!StringUtils.isBlank(domainName)) {
				sb.append(domainName);
			}
			if (!StringUtils.isBlank(serviceAppName)) {
				sb.append(serviceAppName);
			}
			if (!StringUtils.isBlank(serviceVersion)) {
				sb.append(serviceVersion);
			}
			profileUUId = MD5.getInstance().getMD5String(sb.toString());
		}

		return profileUUId;
	}

	public String getParentPath() {
		if (StringUtils.isBlank(parentPath)) {
			try {
				parentPath = ZoneZkUtil.createParentPath(this);
			} catch (InvalidParamException e) {

			}
		}
		return parentPath;
	}

	@Override
	public String toString() {
		return "BaseProfile [rootPath=" + rootPath + ", domainName=" + domainName + ", parentPath=" + parentPath + ", serviceAppName="
				+ serviceAppName + ", serviceName=" + serviceName + ", serviceVersion=" + serviceVersion + ", transProtocol="
				+ transProtocol + "]";
	}

}
