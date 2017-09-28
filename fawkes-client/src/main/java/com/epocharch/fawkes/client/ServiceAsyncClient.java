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
import akka.dispatch.Futures;
import com.epocharch.fawkes.client.actor.MethodActorRepository;
import com.epocharch.fawkes.common.dto.Request;
import com.epocharch.fawkes.common.dto.Response;
import scala.concurrent.Future;
import scala.concurrent.Promise;

/**
 * Created by archer on 19/09/2017.
 */
public class ServiceAsyncClient {

	public Future<Response> callMethod(String serviceName,String methodMengleName,Object[] args,long timeout){
		Request request = RequestBuilder.build(serviceName,methodMengleName,args,timeout);
		ActorRef methodActor = MethodActorRepository.getInstance().getActor(request.getMethodId());
		Promise<Response> promise = Futures.promise();
		request.setPromise(promise);
		methodActor.tell(request, ActorRef.noSender());
		return promise.future();
	}


}
