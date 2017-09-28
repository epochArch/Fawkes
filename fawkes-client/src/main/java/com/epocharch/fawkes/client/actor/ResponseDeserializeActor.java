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
import akka.actor.UntypedAbstractActor;
import com.epocharch.fawkes.client.PendingMsgRepository;
import com.epocharch.fawkes.common.dto.Message;
import com.epocharch.fawkes.common.dto.Request;
import com.epocharch.fawkes.common.dto.Response;
import com.epocharch.fawkes.common.dto.TransShell;
import com.epocharch.fawkes.common.serializer.ISerializerHandler;
import com.epocharch.fawkes.common.serializer.SerializeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archer on 19/09/2017.
 */
public class ResponseDeserializeActor extends UntypedAbstractActor{

	private Logger logger = LoggerFactory.getLogger(ResponseDeserializeActor.class);
	ISerializerHandler<TransShell> shellSerializer;
	public ResponseDeserializeActor() {
		shellSerializer = SerializeFactory.getInstance().getSerialize(SerializeFactory.INTERNAL_SER_SHELL);
	}

	@Override public void onReceive(Object message) throws Throwable {
		if(message instanceof TransShell){
			TransShell<Channel> shell =(TransShell<Channel>)message;
			String type = shell.getType();
			byte[] bytes = shell.getBody();
			ISerializerHandler<Message> ser = SerializeFactory.getInstance().getSerialize(type);
			if(ser!=null){
				Response response = (Response) ser.toObject(bytes);
				ActorRef ackActor = PendingMsgRepository.getInstance().getAckActor(shell.getChannel().id(),response.getMsgId());
				if(ackActor!=null){
					ackActor.tell(response,ActorRef.noSender());
				}
			}else{
				logger.error("Can't find deserializer type:{}"+type);
			}
		}
	}
}
