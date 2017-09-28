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

/**
 * Created by archer on 19/09/2017.
 */
public enum  RouteeStatus {
	ENABLE(1), DISABLE(-1), TEMPORARY_DISABLE(0), UNKNOWN(-2);

	private int code;

	private RouteeStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static RouteeStatus getStatusByCode(int code) {
		for (RouteeStatus ps : RouteeStatus.values()) {
			if (ps.getCode() == code) {
				return ps;
			}
		}
		return RouteeStatus.ENABLE;
	}
}
