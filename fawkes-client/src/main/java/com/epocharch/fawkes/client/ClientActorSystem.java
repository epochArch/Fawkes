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

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.Props;
import akka.routing.DefaultResizer;
import akka.routing.Resizer;
import com.epocharch.common.config.PropertiesContainer;
import com.epocharch.fawkes.common.actor.DeadLetterActor;
import com.epocharch.fawkes.common.constants.Constants;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by archer on 14/09/2017.
 */
public class ClientActorSystem {
	private static Logger logger = LoggerFactory.getLogger(ClientActorSystem.class);

	private static ActorSystem _clientSystem;
	private static String clientName = "client";
	private static Map<String,ActorRef> actorMap = new HashMap<String, ActorRef>();
	private static volatile ClientActorSystem client = new ClientActorSystem();
	private ClientActorSystem() {
		initSystem();
	}


	public ActorSystem getActorSystem() {
		return _clientSystem;
	}

	public static ClientActorSystem getInstance() {
		return client;
	}

	private void initSystem() {
		Properties properties = PropertiesContainer.getInstance().getProperties(Constants.AKKA_PROPERTIES);
		Config config = ConfigFactory.parseProperties(properties).withFallback(ConfigFactory.load());
		_clientSystem = ActorSystem.create(clientName, config);
		ActorRef sdeadLetter = _clientSystem.actorOf(Props.create(DeadLetterActor.class,clientName));
		_clientSystem.eventStream().subscribe(sdeadLetter, DeadLetter.class);
		Resizer resizer = new DefaultResizer(5,100);
		//ActorRef deserializer = _serverSystem.actorOf(new SmallestMailboxPool(5).withResizer(resizer).props(Props.create(DeserializerActor.class)));
		//actorMap.put(ProviderConstants.SERVER_SHELL_ACTOR,shellActor);
	}

	public ActorRef getActor(String name){
		if(actorMap.containsKey(name)){
			return actorMap.get(name);
		}else{
			return null;
		}
	}
}
