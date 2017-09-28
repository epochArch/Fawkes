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

package com.epocharch.fawkes.common.serializer;

import com.epocharch.fawkes.common.dto.TransShell;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by archer on 12/09/2017.
 */
public class ShellSerializer implements ISerializerHandler<TransShell> {

	private Logger logger = LoggerFactory.getLogger(ShellSerializer.class);

	public byte[] toBinary(TransShell shell) {
		byte[] bytes = null;
		if (shell != null) {
			String type = shell.getType();
			String methodId = shell.getMethodId();
			byte[] bodyArr = shell.getBody();
			if (!StringUtils.isBlank(methodId) && !StringUtils.isBlank(type) && bodyArr != null) {
				byte[] methodIdBytes = string2Bytes(methodId);
				byte[] typeBytes = string2Bytes(type);
				int total = methodIdBytes.length + typeBytes.length + bodyArr.length;
				bytes = new byte[total];
				System.arraycopy(methodIdBytes, 0, bytes, 0, methodIdBytes.length);
				System.arraycopy(typeBytes, 0, bytes, methodIdBytes.length, typeBytes.length);
				System.arraycopy(bodyArr, 0, bytes, (methodIdBytes.length + typeBytes.length), bodyArr.length);

			} else {
				logger.error("MethodId,type,body must not null!!!");
			}

		}
		return bytes;
	}

	public TransShell toObject(byte[] bytes) {
		TransShell shell = null;
		if (bytes != null && bytes.length > 0) {
			//parse methodId
			int index = 0;
			int methodIdFlag1 = bytes[index];
			index++;
			int methodIdFlag2 = bytes[index];
			index++;
			int methodIdLen = getLength(methodIdFlag1, methodIdFlag2);
			byte[] methodIdBytes = new byte[methodIdLen];
			System.arraycopy(bytes, index, methodIdBytes, 0, methodIdLen);
			index = index + methodIdLen;
			String methodId = new String(methodIdBytes);
			//parse type
			int typeFlag1 = bytes[index];
			index++;
			int typeFlag2 = bytes[index];
			index++;
			int typelen = getLength(typeFlag1, typeFlag2);
			byte[] typeBytes = new byte[typelen];
			System.arraycopy(bytes, index, typeBytes, 0, typelen);
			index = index + typelen;
			String type = new String(typeBytes);
			//parse body
			int bodylen = bytes.length-index;
			byte[] bodyBytes = new byte[bodylen];
			System.arraycopy(bytes, index, bodyBytes, 0, bodylen);
			shell = new TransShell(methodId, type, bodyBytes);
		}
		return shell;
	}

	/**
	 *
	 * */
	private byte[] string2Bytes(String value) {
		byte[] valueBytes = value.getBytes();
		int len = valueBytes.length;
		byte[] result = new byte[len + 2];
		int flag1 = len / Byte.MAX_VALUE;
		int flag2 = len % Byte.MAX_VALUE;
		result[0]=(byte)flag1;
		result[1]=(byte)flag2;
		System.arraycopy(valueBytes,0,result,2,len);
		return result;
	}

	private int getLength(int flag1, int flag2) {
		return flag1 * Byte.MAX_VALUE + flag2;
	}

}
