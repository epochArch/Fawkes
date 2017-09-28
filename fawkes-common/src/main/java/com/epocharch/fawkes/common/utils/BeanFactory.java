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

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by archer on 15/09/2017.
 */
public class BeanFactory {
	private static BeanFactory beanFactory = new BeanFactory();
	private static Map<String, Object> beanMap = new HashMap<String, Object>();

	private BeanFactory() {

	}

	public static BeanFactory getInstance() {
		return beanFactory;
	}

	/**
	 * Guarantee single instance for className.
	 *
	 */
	public synchronized Object getSingleton(String clazzName, Object... parameters) {
		Object o = null;
		if (!StringUtils.isBlank(clazzName)) {
			if (!beanMap.containsKey(clazzName)) {
				try {
					Class clazz = Class.forName(clazzName);
					Constructor c = clazz.getDeclaredConstructor(getParamTypes(parameters));
					if (c.isAccessible()) {
						o = c.newInstance(parameters);
					} else {
						Method m = clazz.getMethod("getInstance");
						o = m.invoke(null);
					}
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (o != null) {
					beanMap.put(clazzName, o);
				}
			} else {
				o = beanMap.get(clazzName);
			}
		}
		return o;
	}

	private Class[] getParamTypes(Object[] parameters) {
		Class[] paramTypes = null;
		if (parameters != null && parameters.length > 0) {
			int len = parameters.length;
			paramTypes = new Class[len];
			for (int i = 0; i < len; i++) {
				if (parameters[i] != null) {
					Class clz = parameters[i].getClass();
					if (!parameters[i].getClass().isPrimitive()) {
						Class[] ifaces = clz.getInterfaces();
						if (ifaces != null && ifaces.length > 0) {
							clz = ifaces[0];
						}
					}
					paramTypes[i] = clz;

				} else {
					paramTypes[i] = null;
				}
			}
		}
		return paramTypes;
	}

	/**
	 * Guarantee return new instance for each invoke.
	 *
	 */
	public Object newInstance(String clazzName, Object... parameters) {
		Object o = null;
		if (!StringUtils.isBlank(clazzName)) {
			try {
				Class clazz = Class.forName(clazzName);
				Constructor c = clazz.getDeclaredConstructor(getParamTypes(parameters));
				if (c.isAccessible()) {
					o = c.newInstance(parameters);
				} else {
					c.setAccessible(true);
					o = c.newInstance(parameters);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return o;
	}
}
