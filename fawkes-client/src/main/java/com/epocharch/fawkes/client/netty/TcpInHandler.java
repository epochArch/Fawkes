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

package com.epocharch.fawkes.client.netty;

import akka.actor.ActorRef;
import com.epocharch.fawkes.client.router.ClientMethodRepository;
import com.epocharch.fawkes.common.dto.TransShell;
import com.epocharch.fawkes.common.serializer.ISerializerHandler;
import com.epocharch.fawkes.common.serializer.SerializeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archer on 14/09/2017.
 */
public class TcpInHandler extends ChannelInboundHandlerAdapter {

	private Logger logger = LoggerFactory.getLogger(TcpInHandler.class);
	private ISerializerHandler<TransShell> shellSerializer;

	public TcpInHandler(ISerializerHandler<TransShell> shellSerializer) {
		shellSerializer = SerializeFactory.getInstance().getSerialize(SerializeFactory.INTERNAL_SER_SHELL);
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if(msg instanceof ByteBuf){
			ByteBuf buf = (ByteBuf)msg;
			byte[] bytes = new byte[buf.readableBytes()];
			buf.readBytes(bytes);
			TransShell<Channel> shell = shellSerializer.toObject(bytes);
			shell.setChannel(ctx.channel());
			ActorRef desActor = ClientMethodRepository.getInstance().getDeserializer(shell.getMethodId());
			if(desActor!=null){
				desActor.tell(shell, ActorRef.noSender());
			}else {
				logger.error("Can't find deserializer for methodId:"+shell.getMethodId());
			}
		}

	}
}
