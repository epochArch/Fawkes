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

package com.epocharch.fawkes.common.meta;


/**
 * Created by archer on 14/09/2017.
 */
public class ServiceMeta {

	private AppMeta serverAppMeta;
	private ClientMeta clientMeta;
	private String serviceName;
	private String serviceVersion;
	private String groupName;
	private long timeout;
	private boolean autoRedo = false;
	private String balancerType = "WRR";

	public ServiceMeta(AppMeta serverAppMeta, ClientMeta clientMeta) {
		this.serverAppMeta = serverAppMeta;
		this.clientMeta = clientMeta;
	}

	public AppMeta getServerAppMeta() {
		return serverAppMeta;
	}

	public void setServerAppMeta(AppMeta serverAppMeta) {
		this.serverAppMeta = serverAppMeta;
	}

	public ClientMeta getClientMeta() {
		return clientMeta;
	}

	public void setClientMeta(ClientMeta clientMeta) {
		this.clientMeta = clientMeta;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public boolean isAutoRedo() {
		return autoRedo;
	}

	public void setAutoRedo(boolean autoRedo) {
		this.autoRedo = autoRedo;
	}

	public String getBalancerType() {
		return balancerType;
	}

	public void setBalancerType(String balancerType) {
		this.balancerType = balancerType;
	}
}
