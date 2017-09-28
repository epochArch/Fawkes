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

package com.epocharch.fawkes.client.router.balancer;

import com.epocharch.fawkes.client.router.Routee;

import java.util.List;

/**
 * Created by archer on 25/09/2017.
 */
public class BalancerFactory {

	public static IBalancer createBalancer(String type,List<Routee> routees){
		IBalancer balancer = null;
		if(BalancerType.WEIGHT_ROUNDROBIN.getName().equals(type)){
			balancer = new WRRBalancer(routees);
		}
		return balancer;
	}
}
