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

import com.epocharch.common.config.PropertiesContainer;
import com.epocharch.fawkes.common.utils.FawkesUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by archer on 11/09/2017.
 */
public class AppMeta {
	private String appName = "defaultAppName";
	private Properties properties = new Properties();
	private String orgnization = "defaultOrgnization";
	private Map<String,String> extSerializer = new HashMap<String,String>();
	private boolean ssl = false;

	public AppMeta(String appName) {
		this.appName = appName;
		FawkesUtil.setAppId(appName);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public Map<String, String> getExtSerializer() {
		return extSerializer;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setExtSerializer(Map<String, String> extSerializer) {
		this.extSerializer = extSerializer;
	}

	public String getOrgnization() {
		return orgnization;
	}

	public void setOrgnization(String orgnization) {
		this.orgnization = orgnization;
	}
}
