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

package com.epocharch.fawkes.client;

import com.epocharch.fawkes.common.dto.Request;
import com.epocharch.fawkes.common.meta.ServiceMeta;
import com.epocharch.fawkes.common.utils.FawkesUtil;
import com.epocharch.fawkes.common.utils.MethodUtil;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by archer on 14/09/2017.
 */
public class RequestBuilder {

	private static AtomicInteger ticker = new AtomicInteger(0);
	private static final int limit = Integer.MAX_VALUE;
	public static Request build(ServiceMeta serviceMeta,Method method,Object[] args){
		Request request = new Request();
		int t =ticker.incrementAndGet();
		if(t>=limit){
			ticker.set(0);
		}
		request.setMsgId(System.currentTimeMillis()+"-"+t);
		String methodId = MethodUtil
				.genMethodId(FawkesUtil.getAppId(), serviceMeta.getServiceName(), MethodUtil.mangleName(method));
		request.setMethodId(methodId);
		request.setMethodName(method.getName());
		request.setServiceName(serviceMeta.getServiceName());
		request.setTimeout(serviceMeta.getTimeout());
		request.setParameters(args);
		return request;
	}

	public static Request build(String serviceName,String methodMangleName,Object[] args,long timeout){
		Request request = new Request();
		int t =ticker.incrementAndGet();
		if(t>=limit){
			ticker.set(0);
		}
		request.setMsgId(System.currentTimeMillis()+"-"+t);
		request.setServiceName(serviceName);
		String methodId = MethodUtil
				.genMethodId(FawkesUtil.getAppId(), serviceName, methodMangleName);
		request.setMethodId(methodId);
		request.setTimeout(timeout);
		request.setParameters(args);
		return request;
	}

}
