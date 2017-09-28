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

package com.epocharch.fawkes.client.router;

import com.epocharch.fawkes.client.netty.RequestEmitter;
import com.epocharch.fawkes.client.router.balancer.IRelivePolicy;

/**
 * Created by archer on 19/09/2017.
 */
public class PhantomRoutee {
	private Routee routee;
	private RouteeStatus status = RouteeStatus.DISABLE;
	private IRelivePolicy policy;

	public PhantomRoutee(Routee routee) {
		this.routee = routee;
		this.status = routee.getStatus();
		this.policy = new RouteeRelivePolicy(routee);
	}

	public RouteeStatus getStatus() {
		return status;
	}

	public void setStatus(RouteeStatus status) {
		this.status = status;
	}

	public IRelivePolicy getPolicy() {
		return policy;
	}

	public void setPolicy(IRelivePolicy policy) {
		this.policy = policy;
	}

	public Routee getRoutee() {
		return routee;
	}

	public void setRoutee(Routee routee) {
		this.routee = routee;
	}

	public RequestEmitter getEmitter(){
		return routee.getEmitter();
	}
	@Override public String toString() {
		return "PhantomRoutee{" +
				"routee=" + routee.getHostUrl() +
				'}';
	}
}
