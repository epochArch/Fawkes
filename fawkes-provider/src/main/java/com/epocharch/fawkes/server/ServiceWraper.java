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

package com.epocharch.fawkes.server;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by archer on 11/09/2017.
 */
public class ServiceWraper implements InitializingBean,DisposableBean {
	private String name;
	private Class serviceInterface;
	private Object service;
	private String version;
	private boolean initial = false;
	private String serializer = "hessian";
	private int lowbound =10;
	private int highbound =200;

	public void destroy() throws Exception {
		//TODO
		initial = false;
	}

	public void afterPropertiesSet() throws Exception {
		if(StringUtils.isBlank(name)){
			throw new IllegalArgumentException("Name must not blank!!!");
		}
		if(serviceInterface==null){
			throw new IllegalArgumentException("ServiceInterface must not null!!!");
		}
		if(service==null){
			throw new IllegalArgumentException("Service must not null!!!");
		}
		if(StringUtils.isBlank(version)){
			throw new IllegalArgumentException("Name must not blank!!!");
		}
		MethodWorkerRepository.getInstance().deploy(this);
		initial = true;
	}

	public boolean isInitial(){
		return initial;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(Class serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSerializer() {
		return serializer;
	}

	public void setSerializer(String serializer) {
		this.serializer = serializer;
	}

	public Object getService() {
		return service;
	}

	public void setService(Object service) {
		this.service = service;
	}

	public int getLowbound() {
		return lowbound;
	}

	public void setLowbound(int lowbound) {
		this.lowbound = lowbound;
	}

	public int getHighbound() {
		return highbound;
	}

	public void setHighbound(int highbound) {
		this.highbound = highbound;
	}
}
