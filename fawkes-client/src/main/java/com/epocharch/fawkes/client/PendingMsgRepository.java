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

package com.epocharch.fawkes.client;

import akka.actor.ActorRef;
import com.epocharch.fawkes.client.util.ResponseUtil;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by archer on 25/09/2017.
 */
public class PendingMsgRepository {

	private static Logger logger = LoggerFactory.getLogger(PendingMsgRepository.class);
	private static Map<ChannelId,Map<String,ActorRef>> channelMap;
	private static PendingMsgRepository repo = new PendingMsgRepository();

	private PendingMsgRepository(){
		channelMap = new HashMap<ChannelId,Map<String,ActorRef>>();
	}

	public static PendingMsgRepository getInstance(){
		return repo;
	}

	public void add(ChannelId channelId,String msgId,ActorRef ackActor){
		Map<String,ActorRef> msgMap=null;
		if(!channelMap.containsKey(channelId)){
			msgMap= new HashMap<String, ActorRef>();
		}else{
			msgMap = channelMap.get(channelId);
		}
		if(!msgMap.containsKey(msgId)){
			msgMap.put(msgId,ackActor);
		}else{
			ResponseUtil.returnError("Duplicated msgId:" + msgId, ackActor);
		}
	}

	public ActorRef getAckActor(ChannelId channelId,String msgId){
		ActorRef ackActor = null;
		Map<String,ActorRef> msgMap;
		if(channelMap.containsKey(channelId)){
			msgMap = channelMap.get(channelId);
			if(msgMap.containsKey(msgId)){
				ackActor = msgMap.remove(msgId);
			}else{
				logger.error(msgId + " not exist!!!");
			}
		}
		return ackActor;
	}

	public void clean(ChannelId channelId){
		if(channelMap.containsKey(channelId)){
			channelMap.remove(channelId);
		}else{
			logger.error(channelId.asLongText() + " not exist!!!");
		}
	}
}
