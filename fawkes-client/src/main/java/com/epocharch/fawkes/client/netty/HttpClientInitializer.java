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

import com.epocharch.fawkes.common.meta.ClientMeta;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by archer on 14/09/2017.
 */
public class HttpClientInitializer extends ChannelInitializer<SocketChannel> {
	public HttpClientInitializer(ClientMeta cmeta) {
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
		
	}
}
