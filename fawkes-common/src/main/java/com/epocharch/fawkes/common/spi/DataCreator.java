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
package com.epocharch.fawkes.common.spi;

import java.util.Random;

/**
 * @author Archer
 *
 */
public class DataCreator {
	static byte[] Bytesample;
	static {
		Bytesample = new byte[128];
		for (byte i = 0; i < 127; i++) {
			Bytesample[i] = i;
		}
	}

	/**
	 * @param size
	 *            unit is 'k'
	 * @return
	 */
	public static String createObject(int size) {
		byte[] ba = null;
		Random r = new Random();
		if (size > 0) {
			int realSize = size << 10;
			ba = new byte[realSize];
			for (int i = 0; i < realSize; i++) {
				int p = r.nextInt(127);
				ba[i] = Bytesample[p];
			}
		}
		return new String(ba);
	}

	public static String createByteObject(int size) {
		byte[] ba = null;
		Random r = new Random();
		if (size > 0) {
			ba = new byte[size];
			for (int i = 0; i < size; i++) {
				int p = r.nextInt(127);
				ba[i] = Bytesample[p];
			}
		}
		return new String(ba);
	}
}
