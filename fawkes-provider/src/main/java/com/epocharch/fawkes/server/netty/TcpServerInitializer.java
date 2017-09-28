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

import com.epocharch.fawkes.common.meta.ServerMeta;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Created by archer on 05/09/2017.
 */
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {
	private ServerMeta serverMeta;
	public TcpServerInitializer(ServerMeta sMeta) {
		this.serverMeta = sMeta;
	}

	/**
	 * This method will be called once the {@link Channel} was registered. After the method returns this instance
	 * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
	 *
	 * @param ch the {@link Channel} which was registered.
	 * @throws Exception is thrown if an error occurs. In that case it will be handled by
	 *                   {@link #exceptionCaught(ChannelHandlerContext, Throwable)} which will by default close
	 *                   the {@link Channel}.
	 */
	@Override protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		if(serverMeta.getAppMeta().isSsl()){
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}
		p.addLast(new TcpServerHandler());
	}
}
