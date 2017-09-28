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

import akka.actor.UntypedAbstractActor;
import com.epocharch.fawkes.common.dto.TransShell;
import com.epocharch.fawkes.common.serializer.ISerializerHandler;
import com.epocharch.fawkes.common.serializer.SerializeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archer on 28/09/2017.
 */
public class ServerAckActor extends UntypedAbstractActor {

	private ChannelHandlerContext channelCtx;
	private ISerializerHandler<TransShell> shellSerializer;
	private Logger logger = LoggerFactory.getLogger(ServerAckActor.class);

	public ServerAckActor(ChannelHandlerContext channelCtx) {
		this.channelCtx = channelCtx;
		this.shellSerializer = SerializeFactory.getInstance().getSerialize(SerializeFactory.INTERNAL_SER_SHELL);
	}

	@Override public void onReceive(Object message) throws Throwable {
		if(message instanceof TransShell){
			TransShell shell = (TransShell)message;
			byte[] bytes = shellSerializer.toBinary(shell);
			ByteBuf bbuf = Unpooled.copiedBuffer(bytes);
			ChannelFuture future = channelCtx.writeAndFlush(bbuf);
		}else{
			logger.error("Can't process message:"+message);
		}
	}
}
