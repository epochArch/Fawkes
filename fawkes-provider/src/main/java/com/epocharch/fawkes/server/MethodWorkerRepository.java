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

import akka.actor.ActorRef;
import akka.actor.Props;
import com.epocharch.fawkes.common.utils.FawkesUtil;
import com.epocharch.fawkes.common.utils.MethodUtil;
import com.epocharch.fawkes.server.actor.WorkerActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by archer on 12/09/2017.
 */
public class MethodWorkerRepository {
	private static Logger logger = LoggerFactory.getLogger(MethodWorkerRepository.class);

	private Map<String,ActorRef> methodMap;
	private static MethodWorkerRepository repo = new MethodWorkerRepository();
	private MethodWorkerRepository(){
		methodMap = new HashMap<String, ActorRef>();
	}
	public static MethodWorkerRepository getInstance(){
		return repo;
	}

	public void deploy(ServiceWraper serviceWraper){
		Class sInterface = serviceWraper.getServiceInterface();
		Method[] methods = sInterface.getMethods();
		Object service = serviceWraper.getService();
		String sName =serviceWraper.getName();
		for(Method method:methods){
			if(!MethodUtil.isObjectMethod(method.getName())){
				Method sm = null;
				try {
					sm = service.getClass().getMethod(method.getName(), method.getParameterTypes());
					sm.setAccessible(true);
					String methodId = MethodUtil.genMethodId(FawkesUtil.getAppId(), sName, MethodUtil.mangleName(sm));
					ActorRef ref= ServerActorSystem.getInstance().getActorSystem().actorOf(Props.create(WorkerActor.class,sm,service));
					ServerMethodRepository.getInstance().createSerializer(methodId);
					ServerMethodRepository.getInstance().createDeserializer(methodId);
					methodMap.put(methodId,ref);
				} catch (NoSuchMethodException e) {
					logger.error(e.getMessage(),e);
				}

			}
		}
		logger.debug("deploy service {} success!!!!",sName);
	}

	public ActorRef getMethod(String methodId){
		ActorRef ref = null;
		if(methodMap.containsKey(methodId)){
			ref = methodMap.get(methodId);
		}
		return ref;
	}
}
