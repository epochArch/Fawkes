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
import com.epocharch.fawkes.client.PendingMsgRepository;
import com.epocharch.fawkes.client.util.ResponseUtil;
import com.epocharch.fawkes.common.constants.ProtocolType;
import com.epocharch.fawkes.common.dto.TransShell;
import com.epocharch.fawkes.common.meta.ClientMeta;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by archer on 14/09/2017.
 */
public class RequestEmitter {

	private Logger logger = LoggerFactory.getLogger(RequestEmitter.class);
	private Channel _channel;
	private ClientMeta _meta;
	private Bootstrap _bootstrap;
	private int _reconnectCount =0;
	private Lock lock = new ReentrantLock();


	public RequestEmitter(Bootstrap b, ClientMeta meta) {
		this._meta = meta;
		this._bootstrap = b;
		connect();
	}

	public boolean send(final String msgId,TransShell shell, final ActorRef sender) {
		lock.lock();
		boolean isSend = false;
		try{
			if (_channel.isOpen() && _channel.isWritable()) {
				ChannelFuture future= _channel.writeAndFlush(shell);
				future.addListener(new ChannelFutureListener() {
					@Override public void operationComplete(ChannelFuture future) throws Exception {
						if(future.isDone()){
							if(future.isSuccess()){
								PendingMsgRepository.getInstance().add(_channel.id(),msgId,sender);
							}else{
								ResponseUtil.returnError(future.cause(),sender);
							}
						}
					}
				});
				isSend = true;
			} else {
				reconnect();
			}
			return isSend;
		}finally {
			lock.unlock();
		}
	}

	private void connect() {
		try {
			Channel channel = _bootstrap.connect(_meta.getHost(), _meta.getPort()).sync().channel();
			initialHandler();
		} catch (Exception e) {
			String msg = "Can't connect to server " + _meta.getHost() + ":" + _meta.getPort();
			logger.error(msg, e);
		}
	}

	private void initialHandler() {
		if(ProtocolType.TCP.equals(_meta.getProtocol())){
			_channel.pipeline().addLast(new TcpOutHandler());
			_channel.pipeline().addLast(new TcpInHandler());
		}else if(ProtocolType.HTTP.equals(_meta.getProtocol())){

		}

	}

	private void reconnect(){
		_reconnectCount++;
		try {
			ChannelFuture future =_bootstrap.connect(_meta.getHost(), _meta.getPort());
			future.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isDone()){
						if(future.isSuccess()){
							_channel = future.channel();
							_reconnectCount = 0;
						}else {
							long delay = 2l<<_reconnectCount;
							String msg = "Connect failed:"+_meta.getHostStr()+" will be retry after "+delay+"s";
							logger.error(msg,future.cause());
							future.channel().eventLoop().schedule(new Runnable() {
								public void run() {
									reconnect();
								}
							},delay, TimeUnit.SECONDS);
						}
					}
				}
			});
		} catch (Exception e) {
			String msg = "Can't connect to server " + _meta.getHostStr();
			logger.error(msg, e);
		}
	}

}
