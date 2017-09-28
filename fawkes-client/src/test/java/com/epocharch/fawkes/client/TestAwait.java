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
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.util.Timeout;
import com.epocharch.fawkes.client.actor.TestAwaitActor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Promise;

import java.util.concurrent.TimeUnit;

/**
 * Created by archer on 22/09/2017.
 */
public class TestAwait {

	Logger logger = LoggerFactory.getLogger(TestAwait.class);
	@Test
	public void testAwait(){
		ActorSystem actorSystem = ClientActorSystem.getInstance().getActorSystem();
		ActorRef ref = actorSystem.actorOf(Props.create(TestAwaitActor.class));
		Promise p = Futures.promise();
		ref.tell(p,ActorRef.noSender());
		Timeout to = new Timeout(1000, TimeUnit.MILLISECONDS);
		try {
			Await.result(p.future(),to.duration());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

	}
}
