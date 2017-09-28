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

package com.epocharch.fawkes.server.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import com.epocharch.fawkes.common.dto.Message;
import com.epocharch.fawkes.common.dto.Request;
import com.epocharch.fawkes.common.dto.TransShell;
import com.epocharch.fawkes.common.serializer.ISerializerHandler;
import com.epocharch.fawkes.common.serializer.SerializeFactory;
import com.epocharch.fawkes.server.MethodWorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archer on 12/09/2017.
 */
public class RequestDeseralizeActor extends UntypedAbstractActor {

	private Logger logger = LoggerFactory.getLogger(RequestDeseralizeActor.class);
	private ISerializerHandler<TransShell>  shellSerializer;
	private String _methodId;

	public RequestDeseralizeActor(String methodId) {
		this.shellSerializer = SerializeFactory.getInstance().getSerialize(SerializeFactory.INTERNAL_SER_SHELL);
		this._methodId = methodId;
	}

	@Override public void onReceive(Object message) throws Throwable {
		if(message instanceof TransShell){
			TransShell shell = (TransShell) message;
			String type = shell.getType();
			byte[] bytes = shell.getBody();
			ISerializerHandler<Message> ser = SerializeFactory.getInstance().getSerialize(type);
			if(ser!=null){
				Request request = (Request) ser.toObject(bytes);
				ActorRef ref = MethodWorkerRepository.getInstance().getMethod(shell.getMethodId());
				ref.tell(request, getSender());
			}else{
				logger.error("Can't find deserializer type:{}"+type);
			}
		}
	}
}
