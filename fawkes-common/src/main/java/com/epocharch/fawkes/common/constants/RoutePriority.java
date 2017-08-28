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

/**
 * 
 */
package com.epocharch.fawkes.common.constants;

/**
 * @author archer
 *
 */
public enum RoutePriority {

	Primary(Constants.ROUTE_PRIORITY_PRIMARY), Default(Constants.ROUTE_PRIORITY_DEFAULT), Backup(
			Constants.ROUTE_PRIORITY_BACKUP), None(Constants.ROUTE_PRIORITY_NONE);

	private int code;

	private RoutePriority(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static RoutePriority getByCode(int c) {
		for (RoutePriority p : RoutePriority.values()) {
			if (p.getCode() == c) {
				return p;
			}
		}
		return None;
	}
}
