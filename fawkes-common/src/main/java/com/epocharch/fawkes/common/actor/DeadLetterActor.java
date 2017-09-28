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

package com.epocharch.fawkes.common.actor;

import akka.actor.UntypedAbstractActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archer on 14/09/2017.
 */
public class DeadLetterActor extends UntypedAbstractActor{

	private static Logger logger = LoggerFactory.getLogger(DeadLetterActor.class);
	private String flag;

	public DeadLetterActor(String flag) {
		this.flag = flag;
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg != null) {
			logger.warn("{} discard msg:{}",flag,msg.toString());
		}
	}
}
