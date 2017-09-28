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

/**
 * Created by archer on 25/09/2017.
 */
public enum BalancerType {
	TARGET("Target"), ROUNDROBIN("RR"), WEIGHT_ROUNDROBIN("WRR"), CONSISTENT_HASH("CH"), BROADCAST(
			"BC"), LEAST_USAGE("LU");

	private String name;

	private BalancerType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static BalancerType getByName(String name) {
		for (BalancerType r : BalancerType.values()) {
			if (r.getName().equals(name)) {
				return r;
			}
		}
		return WEIGHT_ROUNDROBIN;
	}
}
