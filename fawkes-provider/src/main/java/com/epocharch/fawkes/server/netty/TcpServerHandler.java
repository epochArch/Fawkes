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

package com.epocharch.fawkes.server.netty;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.epocharch.fawkes.ProviderConstants;
import com.epocharch.fawkes.common.dto.TransShell;
import com.epocharch.fawkes.common.serializer.ISerializerHandler;
import com.epocharch.fawkes.common.serializer.SerializeFactory;
import com.epocharch.fawkes.server.ServerActorSystem;
import com.epocharch.fawkes.server.ServerMethodRepository;
import com.epocharch.fawkes.server.actor.ResponseSerializeActor;
import com.epocharch.fawkes.server.actor.ServerAckActor;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archer on 07/09/2017.
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

	private Logger logger = LoggerFactory.getLogger(TcpServerHandler.class);
	private ISerializerHandler<TransShell> shellSerializer;

	public TcpServerHandler(ISerializerHandler<TransShell> shellSerializer) {
		this.shellSerializer = SerializeFactory.getInstance().getSerialize(SerializeFactory.INTERNAL_SER_SHELL);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if(msg instanceof ByteBuf){
			ByteBuf buf = (ByteBuf)msg;
			byte[] shellBytes = new byte[buf.readableBytes()];
			buf.readBytes(shellBytes);
			TransShell shell = shellSerializer.toObject(shellBytes);
			String methodId = shell.getMethodId();
			if(!StringUtils.isBlank(methodId)){
				ActorRef requestDeserializer = ServerMethodRepository.getInstance().getDeserializer(methodId);
				ActorRef ackActor = ServerActorSystem.getInstance().getActorSystem().actorOf(Props.create(ServerAckActor.class,ctx));
				requestDeserializer.tell(shell,ackActor);
			}else{
				logger.error("Can't parse to TransShell");
			}
		} else {
			if (msg != null) {
				logger.error("Can't process data type:" + msg.getClass());
			}else{
				logger.error("Can't process null data");
			}

		}

	}


}
