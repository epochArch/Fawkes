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
import com.epocharch.fawkes.register.IRegister;
import com.epocharch.fawkes.register.ZkRegister;
import com.epocharch.fawkes.server.ServiceWraper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by archer on 11/09/2017.
 */
public class FawkesServer implements InitializingBean,DisposableBean {

	private static Logger logger = LoggerFactory.getLogger(FawkesServer.class);
	private String name="defaultServer";
	private ServerMeta serverMeta;
	private Set<ServiceWraper> serviceSet = new HashSet<ServiceWraper>();
	private IRegister register = new ZkRegister();
	private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	public void destroy() throws Exception {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	public void afterPropertiesSet() throws Exception {
		//TODO
		initServer();
		for(ServiceWraper service:serviceSet){
			register.regist(serverMeta,service);
		}

		//TODO add to admin server
	}

	private void initServer() {
		ChannelInitializer<SocketChannel> initializer = InitializerFactory.getInstance().createInitializer(serverMeta);
		try{

			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workerGroup)
					.channel(NioServerSocketChannel.class)
							//.handler(new LoggingHandler((LogLevel.INFO)))
					.childHandler(initializer);
			Channel ch = b.bind(serverMeta.getPort()).sync().channel();
			logger.info("Bind {} server to {}", serverMeta.getProtocol(), serverMeta.getPort());
		} catch (InterruptedException e) {
			logger.error(serverMeta.getProtocol()+" server initial fail on "+serverMeta.getPort()+"."+e.getMessage(),e);
		}
	}

	public Set<ServiceWraper> getServiceSet() {
		return serviceSet;
	}

	public void setServiceSet(Set<ServiceWraper> serviceSet) {
		this.serviceSet = serviceSet;
	}

	public ServerMeta getServerMeta() {
		return serverMeta;
	}

	public void setServerMeta(ServerMeta serverMeta) {
		this.serverMeta = serverMeta;
	}

}
