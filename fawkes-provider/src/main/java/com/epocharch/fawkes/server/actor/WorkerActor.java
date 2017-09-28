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

package com.epocharch.fawkes.server.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import com.epocharch.fawkes.common.dto.Request;
import com.epocharch.fawkes.common.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by archer on 12/09/2017.
 */
public class WorkerActor extends UntypedAbstractActor {

	private static Logger logger = LoggerFactory.getLogger(WorkerActor.class);

	private Method method;
	private Object service;

	public WorkerActor(Method method, Object service) {
		this.method = method;
		this.service = service;
	}

	@Override public void onReceive(Object message){
		Response response = new Response();
		if(message instanceof Request){
			Request request = (Request)message;
			response.setMsgId(request.getMsgId());
			try {
				Object result = method.invoke(service, request.getParameters());
				response.setResult(result);
			} catch (Throwable e) {
				response.setError(e);
			}
			getSender().tell(response, getSender());

		}else {
			logger.error("Can't process message:"+message.toString());
		}
	}
}
