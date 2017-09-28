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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by archer on 05/09/2017.
 */
public class SerializeFactory {

	private static SerializeFactory factory = new SerializeFactory();
	private static Logger logger = LoggerFactory.getLogger(SerializeFactory.class);
	private Map<String, ISerializerHandler> map = new HashMap<String, ISerializerHandler>();
	private Map<String, ISerializerHandler> extMap = new HashMap<String, ISerializerHandler>();
	public static final String INTERNAL_SER_HESSIAN = "hessian";
	public static final String INTERNAL_SER_SHELL = "shell";

	private SerializeFactory() {
		map.put(INTERNAL_SER_HESSIAN, new HessianSerializer());
		map.put(INTERNAL_SER_SHELL,new ShellSerializer());
	}

	public static SerializeFactory getInstance() {
		return factory;
	}

	public ISerializerHandler getSerialize(String name) {

		if (map.containsKey(name)) {
			return map.get(name);
		} else if (extMap.containsKey(name)) {
			return extMap.get(name);
		}
		return null;
	}

	public void putSerializer(String name, String className) {
		if (!StringUtils.isBlank(className) && !StringUtils.isBlank(name)) {
			try {
				if (!extMap.containsKey(name)) {
					Class clazz = Class.forName(className);
					if (clazz.getInterfaces()[0].equals(ISerializerHandler.class)) {
						ISerializerHandler handler = (ISerializerHandler) clazz.newInstance();
						extMap.put(name, handler);
					} else {
						logger.error("Serializer must implement ISerializerHandler");
					}
				}else{
					logger.error("Serializer {0} is already exist",name);
				}
			} catch (Exception e) {
				logger.error("Can't initialize class {0}", className);
			}
		} else {
			logger.error("Invalid parameter name:{0},className:{1}", name, className);
		}
	}


}
