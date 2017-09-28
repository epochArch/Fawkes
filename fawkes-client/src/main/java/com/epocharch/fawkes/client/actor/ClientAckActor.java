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

import akka.actor.ReceiveTimeout;
import akka.actor.UntypedAbstractActor;
import akka.util.Timeout;
import com.epocharch.fawkes.client.PendingMsgRepository;
import com.epocharch.fawkes.client.util.ExceptionUtil;
import com.epocharch.fawkes.common.dto.Request;
import com.epocharch.fawkes.common.dto.Response;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by archer on 19/09/2017.
 */
public class ClientAckActor extends UntypedAbstractActor {

	private Request request;

	public ClientAckActor(Request request) {
		this.request = request;
		Timeout timeout = new Timeout(request.getTimeout(), TimeUnit.MILLISECONDS);
		Duration duration = timeout.duration();
		this.getContext().setReceiveTimeout(duration);
	}

	@Override public void onReceive(Object message) throws Throwable {
		if(message instanceof Response){
			Response response = (Response)message;
			if(response.isSuccess()){
				request.getPromise().success(response);
			}else {
				request.getPromise().failure(response.getError());
			}
		}else if(message instanceof ReceiveTimeout){
			String msg = "Request timeout!!!";
			Throwable t = ExceptionUtil.createException(msg);
			request.getPromise().failure(t);
		}
	}
}
