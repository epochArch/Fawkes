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

package com.epocharch.fawkes.client.router;

import akka.actor.ActorRef;
import com.epocharch.fawkes.client.router.balancer.IBalancer;
import com.epocharch.fawkes.client.util.ResponseUtil;
import com.epocharch.fawkes.common.dto.Request;
import com.epocharch.fawkes.common.dto.TransShell;

/**
 * Created by archer on 19/09/2017.
 */
public class DefaultRouter implements IRouter {

	private IBalancer<Routee,PhantomRoutee> balancer;
	private String methodId;

	public DefaultRouter(String methodId, IBalancer balancer) {
		this.methodId = methodId;
		this.balancer = balancer;
	}

	public void route(String msgId,TransShell shell,ActorRef sender) {
		PhantomRoutee pr = balancer.select();
		if(pr!=null){
			pr.getEmitter().send(msgId, shell, sender);
		}else{
			String errorMsg = "Can't find routee for method:"+methodId;
			ResponseUtil.returnError(errorMsg,sender);
		}

	}
}
