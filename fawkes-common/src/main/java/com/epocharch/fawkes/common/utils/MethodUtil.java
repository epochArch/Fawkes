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

package com.epocharch.fawkes.common.utils;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by archer on 12/09/2017.
 */
public class MethodUtil {

	public static Set<String> objMethods = new HashSet<String>();
	static {
		Method[] objectMethodArray = Object.class.getMethods();
		for (Method method : objectMethodArray) {
			objMethods.add(method.getName());
		}
	}

	public static boolean isObjectMethod(String methodName) {
		return objMethods.contains(methodName);
	}

	public static String mangleName(Method method) {
		return mangleName(method, false);
	}

	public static String mangleName(Method method, boolean isFull) {
		StringBuffer sb = new StringBuffer();

		sb.append(method.getName());

		Class[] params = method.getParameterTypes();
		for (int i = 0; i < params.length; i++) {
			sb.append('_');
			sb.append(mangleClass(params[i], isFull));
		}
		return sb.toString();
	}
	/**
	 * Mangles a classname.
	 */
	public static String mangleClass(Class cl, boolean isFull) {
		String name = cl.getName();

		if (name.equals("boolean") || name.equals("java.lang.Boolean"))
			return "boolean";
		else if (name.equals("int") || name.equals("java.lang.Integer") || name.equals("short") || name.equals("java.lang.Short")
				|| name.equals("byte") || name.equals("java.lang.Byte"))
			return "int";
		else if (name.equals("long") || name.equals("java.lang.Long"))
			return "long";
		else if (name.equals("float") || name.equals("java.lang.Float") || name.equals("double") || name.equals("java.lang.Double"))
			return "double";
		else if (name.equals("java.lang.String") || name.equals("com.caucho.util.CharBuffer") || name.equals("char")
				|| name.equals("java.lang.Character") || name.equals("java.io.Reader"))
			return "string";
		else if (name.equals("java.util.Date") || name.equals("com.caucho.util.QDate"))
			return "date";
		else if (InputStream.class.isAssignableFrom(cl) || name.equals("[B"))
			return "binary";
		else if (cl.isArray()) {
			return "arr_" + mangleClass(cl.getComponentType(), isFull);
		} else if (name.equals("org.w3c.dom.Node") || name.equals("org.w3c.dom.Element") || name.equals("org.w3c.dom.Document"))
			return "xml";
		else if (isFull)
			return name;
		else {
			int p = name.lastIndexOf('.');
			if (p > 0)
				return name.substring(p + 1);
			else
				return name;
		}
	}

	public static String genMethodId(String appName, String serviceName, String methodMangleName) {
		return new StringBuilder().append(appName).append(".").append(serviceName).append(".").append(methodMangleName).toString();
	}

	public static String genMethodId(String appName, String serviceName){
		return new StringBuilder().append(appName).append(".").append(serviceName).toString();
	}
}
