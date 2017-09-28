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

package com.epocharch.fawkes.client.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.epocharch.fawkes.client.ClientActorSystem;
import com.epocharch.fawkes.client.router.ClientMethodRepository;
import com.epocharch.fawkes.common.meta.ServiceMeta;
import com.epocharch.fawkes.common.utils.MethodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by archer on 14/09/2017.
 */
public class MethodActorRepository {

	private static Logger logger = LoggerFactory.getLogger(MethodActorRepository.class);
	private static volatile MethodActorRepository repo = new MethodActorRepository();
	private Map<String, ActorRef> methodMap;
	private Map<String,List<String>> serviceMethodMap;

	private MethodActorRepository() {
		methodMap = new HashMap<String, ActorRef>();
		serviceMethodMap = new HashMap<String, List<String>>();
	}

	public static MethodActorRepository getInstance() {
		return repo;
	}

	public void deploy(Class serviceInterface, ServiceMeta serviceMeta) {
		Method[] methods = serviceInterface.getMethods();
		String sName = serviceMeta.getServiceName();
		String serviceAppName = serviceMeta.getServerAppMeta().getAppName();
		String serviceId = MethodUtil.genMethodId(serviceAppName, sName);
		for (Method method : methods) {
			if (!MethodUtil.isObjectMethod(method.getName())) {
				String methodId = MethodUtil.genMethodId(serviceAppName, sName, MethodUtil.mangleName(method));
				ActorRef ref = ClientActorSystem.getInstance().getActorSystem().actorOf(Props.create(MethodActor.class, methodId,serviceMeta));
				ClientMethodRepository.getInstance().createSerializer(methodId);
				ClientMethodRepository.getInstance().createDeserializer(methodId);
				methodMap.put(methodId, ref);
				if(!serviceMethodMap.containsKey(serviceId)){
					serviceMethodMap.put(serviceId,new ArrayList<String>());
				}
				serviceMethodMap.get(serviceId).add(methodId);
			}
		}
		logger.debug("deploy service {} success!!!!", sName);
	}

	public ActorRef getActor(String methodId) {
		return methodMap.get(methodId);
	}

	public List<String> getServiceMethods(String serviceId){
		return serviceMethodMap.get(serviceId);
	}

}
