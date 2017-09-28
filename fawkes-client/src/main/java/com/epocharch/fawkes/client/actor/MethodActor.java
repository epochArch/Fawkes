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
import akka.actor.UntypedAbstractActor;
import com.epocharch.fawkes.client.ClientActorSystem;
import com.epocharch.fawkes.client.router.IRouter;
import com.epocharch.fawkes.client.router.ClientMethodRepository;
import com.epocharch.fawkes.client.util.ExceptionUtil;
import com.epocharch.fawkes.common.constants.PropKeys;
import com.epocharch.fawkes.common.dto.Request;
import com.epocharch.fawkes.common.meta.ServiceMeta;
import com.epocharch.fawkes.common.throttler.IThrottler;
import com.epocharch.fawkes.common.throttler.MethodActorThrottler;
import com.epocharch.fawkes.common.utils.BeanFactory;
import com.epocharch.fawkes.common.utils.FawkesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archer on 15/09/2017.
 */
public class MethodActor extends UntypedAbstractActor {

	private Logger logger = LoggerFactory.getLogger(MethodActor.class);
	private ServiceMeta serviceMeta;
	private String methodId;
	private IThrottler methodThrottler;
	private IRouter router;
	private ActorRef serializer;

	public MethodActor(String methodId,ServiceMeta serviceMeta) {
		this.methodId = methodId;
		this.serviceMeta = serviceMeta;
		String throttlerName = FawkesUtil.getProperty(PropKeys.CLIENT_THROTTLE_CLASS);
		Object o = BeanFactory.getInstance().newInstance(throttlerName, this.getContext());
		this.methodThrottler = o == null ? new MethodActorThrottler(this.getContext()) : (IThrottler) o;
		serializer = ClientMethodRepository.getInstance().getSerializer(methodId);
	}

	@Override public void onReceive(Object message) throws Throwable {
		if(message instanceof Request){
			Request request = (Request)message;
			if(methodThrottler.isThrottle(request)){
				ActorRef ack = ClientActorSystem.getInstance().getActorSystem().actorOf(Props.create(ClientAckActor.class, request));
				if(serializer!=null){
					serializer.tell(request,ack);
				}else {
					logger.error("Can't find serializer for methodId" + methodId);
				}
			}else {
				Throwable t = ExceptionUtil.createException("Can't process msg:"+message);
				request.getPromise().failure(t);
			}
		}
	}
}
