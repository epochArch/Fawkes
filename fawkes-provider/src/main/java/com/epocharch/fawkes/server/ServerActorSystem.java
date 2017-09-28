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

import akka.actor.*;
import akka.routing.DefaultResizer;
import akka.routing.Resizer;
import akka.routing.SmallestMailboxPool;
import com.epocharch.common.config.PropertiesContainer;
import com.epocharch.fawkes.ProviderConstants;
import com.epocharch.fawkes.common.actor.DeadLetterActor;
import com.epocharch.fawkes.common.constants.Constants;
import com.epocharch.fawkes.server.actor.RequestDeseralizeActor;
import com.epocharch.fawkes.server.actor.ResponseSerializeActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by archer on 12/09/2017.
 */
public class ServerActorSystem {
	private static Logger logger = LoggerFactory.getLogger(ServerActorSystem.class);

	private static ActorSystem _serverSystem;
	private static String serverName = "server";
	private static Map<String,ActorRef> actorMap = new HashMap<String, ActorRef>();
	private static volatile ServerActorSystem server = new ServerActorSystem();
	private ServerActorSystem() {
		initServerSystem();
	}


	public ActorSystem getActorSystem() {
		return _serverSystem;
	}

	public static ServerActorSystem getInstance() {
		return server;
	}

	private void initServerSystem() {
		Properties properties = PropertiesContainer.getInstance().getProperties(Constants.AKKA_PROPERTIES);
		Config config = ConfigFactory.parseProperties(properties).withFallback(ConfigFactory.load());
		_serverSystem = ActorSystem.create(serverName, config);
		ActorRef sdeadLetter = _serverSystem.actorOf(Props.create(DeadLetterActor.class,serverName));
		_serverSystem.eventStream().subscribe(sdeadLetter, DeadLetter.class);
		Resizer resizer = new DefaultResizer(5,100);
		ActorRef deserializer = _serverSystem.actorOf(new SmallestMailboxPool(5).withResizer(resizer).props(Props.create(RequestDeseralizeActor.class)));
		ActorRef serializer = _serverSystem.actorOf(new SmallestMailboxPool(5).withResizer(resizer).props(Props.create(ResponseSerializeActor.class)));
		actorMap.put(ProviderConstants.SERIALIZER,serializer);
		actorMap.put(ProviderConstants.DESERIALIZER,deserializer);
	}

	public ActorRef getActor(String name){
		if(actorMap.containsKey(name)){
			return actorMap.get(name);
		}else{
			return null;
		}
	}

}
