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

import com.epocharch.common.constants.DeployLevel;
import com.epocharch.fawkes.client.netty.FawkesClient;
import com.epocharch.fawkes.client.netty.RequestEmitter;
import com.epocharch.fawkes.client.router.RouteeStatus;
import com.epocharch.fawkes.common.constants.RoutePriority;

/**
 * Created by archer on 20/09/2017.
 */
public class Routee {
	private String name;
	private String hostUrl;
	private String zone="fawkesZone";
	private String idc="fawkesIDC";
	private RequestEmitter emitter;
	private DeployLevel level= DeployLevel.IDC;
	private RouteeStatus status = RouteeStatus.ENABLE;
	private int weight=1;
	private RoutePriority priority = RoutePriority.None;

	public Routee(String name, String hostUrl) {
		this.name = name;
		this.hostUrl = hostUrl;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getIdc() {
		return idc;
	}

	public void setIdc(String idc) {
		this.idc = idc;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public RoutePriority getPriority() {
		return priority;
	}

	public void setPriority(RoutePriority priority) {
		this.priority = priority;
	}

	public RouteeStatus getStatus() {
		return status;
	}

	public void setStatus(RouteeStatus status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DeployLevel getLevel() {
		return level;
	}

	public void setLevel(DeployLevel level) {
		this.level = level;
	}

	public RequestEmitter getEmitter() {
		return emitter;
	}

	public void setEmitter(RequestEmitter emitter) {
		this.emitter = emitter;
	}
}
