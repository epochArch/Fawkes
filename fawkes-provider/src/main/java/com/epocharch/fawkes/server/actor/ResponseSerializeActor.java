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
import com.epocharch.fawkes.common.dto.Response;
import com.epocharch.fawkes.common.dto.TransShell;
import com.epocharch.fawkes.common.serializer.ISerializerHandler;
import com.epocharch.fawkes.common.serializer.SerializeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archer on 12/09/2017.
 */
public class ResponseSerializeActor extends UntypedAbstractActor {

	private Logger logger = LoggerFactory.getLogger(ResponseSerializeActor.class);
	private ISerializerHandler<TransShell> shellSerializer;
	private String methodId;

	public ResponseSerializeActor(String methodId){
		this.methodId = methodId;
		shellSerializer = SerializeFactory.getInstance().getSerialize(SerializeFactory.INTERNAL_SER_SHELL);
	}

	@Override public void onReceive(Object message) throws Throwable {
		if(message instanceof Response){
			Response response = (Response)message;
			String type = response.getSerializeType();
			ISerializerHandler<Message> respSer = SerializeFactory.getInstance().getSerialize(type);
			byte[] respBytes = respSer.toBinary(response);
			TransShell shell = new TransShell(methodId,type,respBytes);
			getSender().tell(shell, ActorRef.noSender());
		}else{
			logger.error("Can't process msg:"+message);
		}
	}
}
