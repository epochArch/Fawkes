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
import com.epocharch.fawkes.common.dto.Request;

/**
 * Created by archer on 12/09/2017.
 */
public class Dispatcher {

	private static Dispatcher dispatcher = new Dispatcher();

	private Dispatcher(){

	}

	public static Dispatcher getInstance(){
		return dispatcher;
	}

	public void dispatch(Request request,ActorRef sender){
		String methodId = request.getMethodId();
		ActorRef ref = MethodWorkerRepository.getInstance().getMethod(methodId);
		ref.tell(request,sender);
	}
}
