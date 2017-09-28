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
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.DefaultResizer;
import akka.routing.Resizer;
import akka.routing.SmallestMailboxPool;
import com.epocharch.fawkes.server.actor.RequestDeseralizeActor;
import com.epocharch.fawkes.server.actor.ResponseSerializeActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by archer on 26/09/2017.
 */
public class ServerMethodRepository {

	private static Logger logger = LoggerFactory.getLogger(ServerMethodRepository.class);
	private Map<String,ActorRef> _deserializerMap;
	private Map<String,ActorRef> _serializerMap;
	private ActorSystem _actorSystem;
	private static volatile ServerMethodRepository repo = new ServerMethodRepository();

	private ServerMethodRepository() {
		_deserializerMap = new HashMap<String, ActorRef>();
		_serializerMap = new HashMap<String, ActorRef>();
		_actorSystem = ServerActorSystem.getInstance().getActorSystem();
	}

	public static ServerMethodRepository getInstance() {
		return repo;
	}

	public void createSerializer(String methodId){
		Resizer resizer = new DefaultResizer(5,100);
		ActorRef serializer = _actorSystem.actorOf(
				new SmallestMailboxPool(5).withResizer(resizer).props(Props.create(ResponseSerializeActor.class, methodId)));
		_serializerMap.put(methodId,serializer);
	}

	public void createDeserializer(String methodId){
		Resizer resizer = new DefaultResizer(5,100);
		ActorRef deserializer = _actorSystem.actorOf(
				new SmallestMailboxPool(5).withResizer(resizer).props(Props.create(RequestDeseralizeActor.class, methodId)));
		_deserializerMap.put(methodId,deserializer);
	}

	public ActorRef getSerializer(String methodId){
		return _serializerMap.get(methodId);
	}

	public ActorRef getDeserializer(String methodId){
		return _deserializerMap.get(methodId);
	}
}
