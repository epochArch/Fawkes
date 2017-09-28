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

package com.epocharch.fawkes.common.dto;

import org.jboss.netty.channel.ChannelHandlerContext;
import scala.concurrent.Promise;

/**
 * Created by archer on 06/09/2017.
 */
public class Request extends Message {

	private static final long serialVersionUID = 6017051668068324953L;

	private Object[] parameters;
	private long timeout;
	private String methodName;
	private String methodId;
	private String serviceName;
	private transient Promise<Response> promise;
	private ChannelHandlerContext channelCtx;

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodId() {
		return methodId;
	}

	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}

	public Promise<Response> getPromise() {
		return promise;
	}

	public void setPromise(Promise<Response> promise) {
		this.promise = promise;
	}
}
