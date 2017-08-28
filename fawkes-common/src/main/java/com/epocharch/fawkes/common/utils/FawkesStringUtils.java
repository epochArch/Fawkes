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
package com.epocharch.fawkes.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author archer
 * 
 */
public class FawkesStringUtils {
	private static Pattern numPattern = Pattern.compile("-?[0-9]*");
	private static Pattern ipPattern = Pattern.compile(
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
					+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	private static Pattern ipPortPattern = Pattern.compile(
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
					+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5]):(\\d{1,5})$");

	// private static String poolIdPattern = "\\w+/\\w+[-?\\w+]+";
	/**
	public static boolean isPoolid(String str) {
		boolean b = false;
		if (!StringUtils.isBlank(str)) {
			b = str.matches(poolIdPattern);
		}
		return b;
	}
	*/
	public static boolean isNumeric(String str) {
		boolean b = false;
		if (!StringUtils.isBlank(str)) {
			Matcher isNum = numPattern.matcher(str.trim());
			if (isNum.matches()) {
				b = true;
			}
		}
		return b;
	}

	public static boolean isIpAddress(String str) {
		boolean b = false;
		if (!StringUtils.isBlank(str)) {
			Matcher isIp = ipPattern.matcher(str);
			if (isIp.matches()) {
				b = true;
			}
		}
		return b;
	}

	public static boolean isIpPortAddress(String str) {
		boolean b = false;
		if (!StringUtils.isBlank(str)) {
			Matcher isIpPort = ipPortPattern.matcher(str);
			if (isIpPort.matches()) {
				b = true;
			}
		}
		return b;
	}

	public static String addressInt2Ip4(int addInt) {
		int address = addInt;
		byte[] addr = new byte[4];

		addr[0] = (byte) ((address >>> 24) & 0xFF);
		addr[1] = (byte) ((address >>> 16) & 0xFF);
		addr[2] = (byte) ((address >>> 8) & 0xFF);
		addr[3] = (byte) (address & 0xFF);
		return numericToTextFormat(addr);
	}

	public static String numericToTextFormat(byte[] src) {
		return (src[0] & 0xff) + "." + (src[1] & 0xff) + "." + (src[2] & 0xff) + "." + (src[3] & 0xff);
	}

	public static String replaceSlash(String value) {
		String v = null;
		if (!StringUtils.isBlank(value)) {
			v = value.replace("/", "#");
		}
		return v;
	}

	public static String limitString(String value, int limit) {
		if (value != null && limit > 0 && value.length() > limit) {
			value = value.substring(0, limit);
		}
		return value;
	}
}
