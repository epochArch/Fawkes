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

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.epocharch.fawkes.common.dto.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by archer on 06/09/2017.
 */
public class HessianSerializer implements ISerializerHandler<Message> {

	@Override public byte[] toBinary(Message msg) {
		byte[] arr = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(5120);
		Hessian2Output ho = new Hessian2Output(bos);
		try {
			ho.startMessage();
			ho.writeObject(msg);
			ho.completeMessage();
			ho.close();
			arr = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
			}
		}
		return arr;
	}

	@Override public Message toObject(byte[] byteArry) {
		Message msg = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArry);
		Hessian2Input in = new Hessian2Input(bis);
		try {
			Object o = null;
			in.startMessage();
			o = in.readObject();
			if(o!=null){
				msg = (Message)o;
			}
			in.completeMessage();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return msg;
	}
}
