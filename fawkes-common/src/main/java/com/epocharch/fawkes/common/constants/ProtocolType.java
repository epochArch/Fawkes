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
package com.epocharch.fawkes.common.constants;

/**
 * @author archer
 *
 */
public enum ProtocolType {
	
	NONE("NONE"),
	HTTP(SupportedProtocol.HTTP),
	HTTPS(SupportedProtocol.HTTPS),
	TCP(SupportedProtocol.TCP),
	UDP(SupportedProtocol.UDP),
	AKKAtcp(SupportedProtocol.AKKAtcp),
	AKKAudp(SupportedProtocol.AKKAudp);
	
	private String prefix;
	private ProtocolType(String prefix){
		this.prefix = prefix;
	}
	public String getPrefix() {
		return prefix;
	}
	
	public static ProtocolType getByPrefix(String prefix){
		for(ProtocolType p:ProtocolType.values()){
			if(p.getPrefix().equals(prefix)){
				return p;
			}
		}
		return NONE;
	}
	
}
