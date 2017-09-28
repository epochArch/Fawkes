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

import com.epocharch.fawkes.common.constants.ProtocolType;

import java.util.HashMap;

/**
 * Created by archer on 06/09/2017.
 */
public class ClientMeta extends BaseMeta {

	private String host = "127.0.0.1";
	private String hostStr;

	public ClientMeta(AppMeta appMeta) {
		super.setAppMeta(appMeta);
	}

	public ClientMeta(AppMeta appMeta,ProtocolType protocol, String host,int port) {
		super.setAppMeta(appMeta);
		this.host = host;
		super.setPort(port);
		super.setProtocol(protocol);
		hostStr = host+":"+port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHostStr() {
		return hostStr;
	}

	@Override public boolean equals(Object obj) {
		boolean value = false;
		if(obj instanceof ClientMeta){
			ClientMeta clientMeta = (ClientMeta)obj;
			value = this.getHostStr().equals(clientMeta.getHostStr());
		}
		return value;
	}
}
