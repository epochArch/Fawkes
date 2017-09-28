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

import com.epocharch.fawkes.client.util.ExceptionUtil;
import com.epocharch.fawkes.common.dto.TransShell;
import com.epocharch.fawkes.common.serializer.ISerializerHandler;
import com.epocharch.fawkes.common.serializer.SerializeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

/**
 * Created by archer on 14/09/2017.
 */
public class TcpOutHandler extends ChannelOutboundHandlerAdapter {

	private ISerializerHandler<TransShell> shellSerializer;

	public TcpOutHandler() {
		shellSerializer = SerializeFactory.getInstance().getSerialize(SerializeFactory.INTERNAL_SER_SHELL);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if(msg instanceof TransShell){
			TransShell shell = (TransShell)msg;
			byte[] bytes = shellSerializer.toBinary(shell);
			ByteBuf bbuf = Unpooled.copiedBuffer(bytes);
			ChannelFuture future = ctx.writeAndFlush(bbuf);
		}else{
			throw ExceptionUtil.createException("Can't process msg:" + msg);
		}

	}
}
