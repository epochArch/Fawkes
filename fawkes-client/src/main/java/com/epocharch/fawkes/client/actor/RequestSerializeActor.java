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

import akka.actor.UntypedAbstractActor;
import com.epocharch.fawkes.client.router.IRouter;
import com.epocharch.fawkes.client.router.ClientMethodRepository;
import com.epocharch.fawkes.client.util.ResponseUtil;
import com.epocharch.fawkes.common.dto.Request;
import com.epocharch.fawkes.common.dto.TransShell;
import com.epocharch.fawkes.common.serializer.ISerializerHandler;
import com.epocharch.fawkes.common.serializer.SerializeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archer on 12/09/2017.
 */
public class RequestSerializeActor extends UntypedAbstractActor {

	private Logger logger = LoggerFactory.getLogger(RequestSerializeActor.class);

	private ISerializerHandler<TransShell> shellSerializer;
	private String _methodId;
	public RequestSerializeActor(String methodId) {
		shellSerializer = SerializeFactory.getInstance().getSerialize(SerializeFactory.INTERNAL_SER_SHELL);
		this._methodId = methodId;
	}

	@Override public void onReceive(Object message) throws Throwable {
		if(message instanceof Request){
			Request request = (Request)message;
			String type = request.getSerializeType();
			ISerializerHandler<Request> serializerHandler = SerializeFactory.getInstance().getSerialize(type);
			if(serializerHandler!=null){
				byte[] reqBytes =  serializerHandler.toBinary(request);
				TransShell shell = new TransShell(_methodId,type,reqBytes);
				byte[] shellBytes = shellSerializer.toBinary(shell);
				IRouter router = ClientMethodRepository.getInstance().getRouter(_methodId);
				if(router!=null){
					router.route(request.getMsgId(),shell,getSender());
				}else {
					String msg = "Can't find router for method:"+ _methodId;
					logger.error(msg);
					ResponseUtil.returnError(msg,getSender());
				}

			}else {
				logger.error("Can't find serializer type:{}"+type);
			}

		}
	}
}
