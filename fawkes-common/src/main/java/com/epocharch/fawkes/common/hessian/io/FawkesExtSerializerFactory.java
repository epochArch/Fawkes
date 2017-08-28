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
package com.epocharch.fawkes.common.hessian.io;

import com.caucho.hessian.io.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Archer
 * 
 */
public class FawkesExtSerializerFactory extends SerializerFactory {

	private static Map<Class, Serializer> _extSerializerMap = new HashMap<Class, Serializer>();
	private static Map<Class, Deserializer> _extDeserializerMap = new HashMap<Class, Deserializer>();
	private static SerializerFactory factory = new FawkesExtSerializerFactory();

	public static SerializerFactory createFactory() {
		return factory;
	}

	private FawkesExtSerializerFactory() {
		super();
	}

	@Override
	public Serializer getSerializer(Class clazz) throws HessianProtocolException {
		Serializer s = _extSerializerMap.get(clazz);
		if (s == null) {
			s = super.getSerializer(clazz);
		}
		return s;
	}

	@Override
	public Deserializer getDeserializer(Class clazz) throws HessianProtocolException {
//		if(clazz.getName().equals("java.util.EnumSet$SerializationProxy")){
//			_extDeserializerMap.put(clazz, new EnumSetDeserializer());
//		}
		Deserializer d = _extDeserializerMap.get(clazz);
		if (d == null) {
			d = super.getDeserializer(clazz);
		}
		return d;
	}

	static {
		_extSerializerMap.put(BigDecimal.class, new StringValueSerializer());
		_extSerializerMap.put(BigInteger.class, new StringValueSerializer());
//		_extSerializerMap.put(Byte.class, new StringValueSerializer());
//		_extSerializerMap.put(Short.class, new StringValueSerializer());
//		_extSerializerMap.put(Float.class, new StringValueSerializer());

		_extDeserializerMap.put(BigDecimal.class, new BigDecimalDeserializer());
		_extDeserializerMap.put(BigInteger.class, new BigIntegerDeserializer());
//		_extDeserializerMap.put(Byte.class, new ByteDeserializer());
//		_extDeserializerMap.put(Short.class, new ShortDeserializer());
//		_extDeserializerMap.put(Float.class, new FloatDeserializer());

		_extDeserializerMap.put(Date.class, new SqlDateDeserializer(Date.class));
	}
}
