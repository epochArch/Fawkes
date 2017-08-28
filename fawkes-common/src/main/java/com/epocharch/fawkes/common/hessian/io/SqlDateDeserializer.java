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

import com.caucho.hessian.HessianException;
import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.IOExceptionWrapper;

import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * Deserializing a string valued object
 */
public class SqlDateDeserializer extends AbstractDeserializer {
	private Class _cl;
	private Constructor _constructor;

	public SqlDateDeserializer(Class cl) {
		try {
			_cl = cl;
			_constructor = cl.getConstructor(new Class[] { long.class });
		} catch (NoSuchMethodException e) {
			throw new HessianException(e);
		}
	}

	public Class getType() {
		return _cl;
	}

	public Object readMap(AbstractHessianInput in) throws IOException {
		int ref = in.addRef(null);

		long initValue = Long.MIN_VALUE;

		while (!in.isEnd()) {
			String key = in.readString();

			if (key.equals("value"))
				initValue = in.readUTCDate();
			else
				in.readString();
		}

		in.readMapEnd();

		Object value = create(initValue);

		in.setRef(ref, value);

		return value;
	}

	public Object readObject(AbstractHessianInput in, Object[] fields) throws IOException {
		String[] fieldNames = (String[]) fields;

		int ref = in.addRef(null);

		long initValue = Long.MIN_VALUE;

		for (int i = 0; i < fieldNames.length; i++) {
			String key = fieldNames[i];

			if (key.equals("value"))
				initValue = in.readUTCDate();
			else
				in.readObject();
		}

		Object value = create(initValue);

		in.setRef(ref, value);

		return value;
	}

	private Object create(long initValue) throws IOException {
		if (initValue == Long.MIN_VALUE)
			throw new IOException(_cl.getName() + " expects name.");

		try {
			return _constructor.newInstance(new Object[] { new Long(initValue) });
		} catch (Exception e) {
			throw new IOExceptionWrapper(e);
		}
	}
}
