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

import com.epocharch.fawkes.common.constants.ProtocolType;
import com.epocharch.fawkes.common.meta.ClientMeta;
import com.epocharch.fawkes.common.meta.ServerMeta;
import com.epocharch.fawkes.common.serializer.SerializeFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

/**
 * Created by archer on 14/09/2017.
 */
public class InitializerFactory {

	private static InitializerFactory initializerFactory = new InitializerFactory();

	public InitializerFactory(){

	}

	public static InitializerFactory getInstance(){
		return initializerFactory;
	}

	public ChannelInitializer<SocketChannel> createInitializer(ClientMeta cmeta){
		initialSerializer(cmeta.getAppMeta().getExtSerializer());
		ChannelInitializer<SocketChannel> initializer = null;
		if(cmeta.getProtocol().equals(ProtocolType.HTTP)){
			initializer = new HttpClientInitializer(cmeta);
		}else if(cmeta.getProtocol().equals(ProtocolType.TCP)){
			initializer = new TcpClientInitializer(cmeta);
		}
		return initializer;
	}



	public void initialSerializer(Map<String,String> extSerializers){
		if(extSerializers!=null){
			for(Map.Entry<String,String> es :extSerializers.entrySet()){
				SerializeFactory.getInstance().putSerializer(es.getKey(),es.getValue());
			}
		}

	}
}
