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
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by archer on 14/09/2017.
 */
public class FawkesClient {

	private Logger logger = LoggerFactory.getLogger(FawkesClient.class);
	private EventLoopGroup group = new NioEventLoopGroup();
	private Bootstrap b = new Bootstrap();
	private Map<ClientMeta,RequestEmitter> emitterMap = new HashMap<ClientMeta, RequestEmitter>();
	public FawkesClient(){
		b.group(group).channel(NioSocketChannel.class);
	}

	public RequestEmitter getEmitter(ClientMeta meta){
		RequestEmitter emitter = null;
		if(emitterMap.containsKey(meta)){
			emitter = emitterMap.get(meta);
		}else{
			emitter = new RequestEmitter(b,meta);
			emitterMap.put(meta,emitter);
		}
		return emitter;
	}
}
