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

package com.epocharch.fawkes.common.throttler;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.actor.UntypedActorContext;
import com.epocharch.fawkes.common.constants.PropKeys;
import com.epocharch.fawkes.common.dto.Request;
import com.epocharch.fawkes.common.utils.FawkesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archer on 15/09/2017.
 */
public class MethodActorThrottler implements IThrottler{
	private Logger logger = LoggerFactory.getLogger(MethodActorThrottler.class);
	private AbstractActor.ActorContext methodActor;
	private int capacity = FawkesUtil.getIntProperty(PropKeys.CLIENT_THROTTLE, 100000);

	public MethodActorThrottler(AbstractActor.ActorContext context) {
		this.methodActor = context;
	}

	public boolean isThrottle(Request request) {
		boolean result = false;
		int acSize = 0;
		scala.collection.immutable.Iterable<ActorRef> iter = methodActor.children();
		if (iter != null) {
			acSize = iter.size();
			if(logger.isDebugEnabled()){
				logger.debug(methodActor.toString() + " current ackActor count:" + acSize);
			}
		}
		result = acSize > capacity;
		return result;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;

	}

	public int getCapacity() {
		return 0;
	}
}
