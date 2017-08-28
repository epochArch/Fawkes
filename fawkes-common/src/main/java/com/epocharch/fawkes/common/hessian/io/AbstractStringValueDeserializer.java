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

package com.epocharch.fawkes.common.hessian.io;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;

import java.io.IOException;

/**
 * Deserializes a string-valued object like BigDecimal.
 */
abstract public class AbstractStringValueDeserializer extends AbstractDeserializer {
	abstract protected Object create(String value) throws IOException;

	@Override
	public Object readMap(AbstractHessianInput in) throws IOException {
		String value = null;

		while (!in.isEnd()) {
			String key = in.readString();

			if (key.equals("value"))
				value = in.readString();
			else
				in.readObject();
		}

		in.readMapEnd();

		Object object = create(value);

		in.addRef(object);

		return object;
	}

	public Object readObject(AbstractHessianInput in, Object[] fields) throws IOException {
		String[] fieldNames = (String[]) fields;

		String value = null;

		for (int i = 0; i < fieldNames.length; i++) {
			if ("value".equals(fieldNames[i]))
				value = in.readString();
			else
				in.readObject();
		}

		Object object = create(value);

		in.addRef(object);

		return object;
	}
}
