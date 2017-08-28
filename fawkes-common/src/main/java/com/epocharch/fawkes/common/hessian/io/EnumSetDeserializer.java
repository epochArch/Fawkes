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

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractMapDeserializer;

import java.io.IOException;
import java.util.EnumSet;

/**
 * @author archer
 * 
 */
public class EnumSetDeserializer extends AbstractMapDeserializer {

	public Object readMap(AbstractHessianInput in){
		EnumSet es = null;
		try {
			es= readEnumSet(in, Enum.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return es;
	}

	private static class EnumSetSerializationProxy<E extends Enum<E>> {
		Object elementType;
		Enum<E>[] elements;
	}

	private <E extends Enum<E>> EnumSet<E> readEnumSet(AbstractHessianInput in,
			Class<E> klass) throws IOException {
		EnumSetSerializationProxy<E> essp = new EnumSetSerializationProxy<E>();
		int ref = in.addRef(essp);
		in.readObject();
		essp.elementType = in.readObject();
		in.readObject();
		essp.elements = (Enum<E>[])in.readObject();
		EnumSet<E> es = null;
		if(essp.elements!=null){
			es = EnumSet.noneOf((Class<E>) essp.elementType);
			for (Object element : essp.elements)
				es.add((E) element);	
		}
		
		return es;
	}
}
