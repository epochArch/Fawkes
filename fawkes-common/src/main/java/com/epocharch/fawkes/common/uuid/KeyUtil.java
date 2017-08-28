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

package com.epocharch.fawkes.common.uuid;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by archer on 28/08/2017.
 */
public class KeyUtil {

	private static final int threshold = 10000000;
	private static final int partions = 1000;
	private static final String separator = "-";
	private static final int maxLength = 18;

	static Integer jvmPidHashcode = null;

	public static Integer getJvmPidHashCode() {
		if (jvmPidHashcode == null) {
			try {
				jvmPidHashcode = ManagementFactory.getRuntimeMXBean().getName().hashCode();
			} catch (Exception e) {
				jvmPidHashcode = (int) (Math.random() * (100000 + 1) - 1);
			}
		}
		return jvmPidHashcode;
	}


	private static long initValue() {
		long v1 = System.currentTimeMillis();
		long v2 = (v1 / (1000 * 60)) * (1000 * 60);
		return v1 - v2;
	}

	private static final AtomicLong curtValue = new AtomicLong(initValue());
	//
	public static String getGlobalId(String appId){
		StringBuilder sb = new StringBuilder();
		sb.append(appId+"-");
		sb.append(System.currentTimeMillis()+"-");
		sb.append(getJvmPidHashCode()+"");
		long curt = curtValue.incrementAndGet();
		if (curt >= threshold) {
			curtValue.set(0);
		}
		sb.append(curtValue);
		return  sb.toString();
	}


}
