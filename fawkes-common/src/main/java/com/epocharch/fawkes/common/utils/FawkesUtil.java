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
package com.epocharch.fawkes.common.utils;

import com.epocharch.common.config.PropertiesContainer;
import com.epocharch.fawkes.common.constants.Constants;
import com.epocharch.fawkes.common.constants.ZkPathConstants;
import com.epocharch.fawkes.common.dto.BaseProfile;
import com.epocharch.fawkes.common.dto.ServiceProfile;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Archer Jiang
 * 
 */
public class FawkesUtil {

	public static String getRawClassName(Object object) {
		if (object != null) {
			if (AopUtils.isAopProxy(object)) {
				String v = AopUtils.getTargetClass(object).getSimpleName();
				if (v.contains("$Proxy") || v.contains("$$")) {
					try {
						Object o = ((Advised) object).getTargetSource().getTarget();
						return getRawClassName(o);
					} catch (Exception e) {
					}
				} else {
					return v;
				}
			} else if (AopUtils.isCglibProxy(object)) {
				return AopUtils.getTargetClass(object).getSimpleName();
			} else {
				return object.getClass().getSimpleName();
			}
		}
		return "UnknowClass";
	}

	public static String generateKey(BaseProfile profile) {
		StringBuilder sb = new StringBuilder(profile.getServiceAppName());
		sb.append("_").append(profile.getServiceName()).append("_").append(profile.getServiceVersion());
		return sb.toString();
	}

	public static String getChildFullPath(String parentPath, String shortChildPath) {
		return parentPath + "/" + shortChildPath;
	}

	public static String getChildShortPath(String fullPath) {
		return fullPath.substring(fullPath.lastIndexOf("/") + 1);
	}

	public static String list2String(List<String> list) {
		StringBuilder sb = new StringBuilder("[");
		if (list != null) {
			for (String o : list) {
				if (o != null) {
					sb.append(o.toString()).append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}


	public static boolean isEmptyList(List list) {
		return list == null || list.size() == 0;
	}

	public static String generateServiceUrl(ServiceProfile sp) {
		String prefix = sp.getProtocolPrefix();
		StringBuilder sb = new StringBuilder();
		if (prefix.equals(Constants.PROTOCOL_PROFIX_HTTP)) {
			sb.append(prefix).append("://").append(sp.getHostIp()).append(":").append(sp.getPort()).append("/");
			if (sp.isAssembleAppName()) {
				sb.append(sp.getServiceAppName()).append("/");
			}
			sb.append(sp.getUrlPattern()).append("/").append(sp.getServiceName());
		} else if (prefix.equals(Constants.PROTOCOL_PROFIX_AKKATCP)) {
			sb.append(prefix).append("://").append(sp.getHostIp()).append(":").append(sp.getPort());
		}

		return sb.toString();
	}

	public static int parseString2Int(String value, int defaultValue) {
		int i = defaultValue;
		try {
			i = Integer.valueOf(value);
		} catch (Exception e) {
		}
		return i;
	}

	public static String generateHandlerName(Class clz) {
		return clz.getName();
	}

	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public static long getCurrentNanoTime() {
		return System.nanoTime();
	}

	public static String getHostFromUrl(String url) {
		String value = "";
		if (!StringUtils.isBlank(url)) {
			String[] arr = url.split("/", 6);
			value = arr[2];
		}
		return value;
	}

	public static String getMethodName(MethodInvocation invocation) {
		String value = "unknowMethod";
		if (invocation != null) {
			value = invocation.getMethod().getName();
		}
		return value;
	}

	public static String getClassName(MethodInvocation invocation) {
		String value = "unKnowClass";
		if (invocation != null) {
			value = invocation.getMethod().getDeclaringClass().getSimpleName();
		}
		return value;
	}

	public static String getShortClassName(String clazzName) {
		String value = clazzName;
		if (clazzName != null) {
			String[] arr = clazzName.split("\\.");
			if (arr != null && arr.length > 0) {
				value = arr[(arr.length - 1)];
			}
		}
		return value;
	}

	public static String getErrorMsg(Throwable ex) {
		String value = "";
		if (ex != null) {
			value = ex.getMessage();
			if (StringUtils.isBlank(value)) {
				value = ex.getClass().getName();
			}
		}
		return value;
	}

	public static String filterString(String value) {
		return value.replaceAll("/", "_");
	}

	public static String replaceSlash(String value) {
		String v = "unknowPoolName";
		if (!StringUtils.isBlank(value)) {
			v = value.replaceAll("/", "#");
		}
		return v;
	}

	public static Object getPrivateField(Object source, String filedName) {
		Object o = null;
		if (source != null) {
			Class clazz = source.getClass();
			try {
				Field field = clazz.getDeclaredField(filedName);
				field.setAccessible(true);
				o = field.get(source);
			} catch (Exception e) {

			}
		}
		return o;
	}

	public static String getPrivateStringField(Object source, String filedName) {
		Object o = getPrivateField(source, filedName);
		return o == null ? null : (String) o;
	}

	public static String genAuthorization(String user, String passwd) {
		String enStr = user + ":" + passwd;
		return "Basic " + new String(Base64.encodeBase64(enStr.getBytes()));
	}

	public static String genPoolFlagsPath(String poolName) {
		StringBuilder sb = new StringBuilder();
		sb.append(ZkPathConstants.BASE_ROOT_FLAGS).append("/").append(replaceSlash(poolName));
		return sb.toString();
	}

	public static String getProperty(String key){
		return PropertiesContainer.getInstance().getPropertyByNameSpace(Constants.NAMESPACE_FAWKES, key);
	}

	public static String getProperty(String key,String defaultVale){
		return PropertiesContainer.getInstance().getPropertyByNameSpace(Constants.NAMESPACE_FAWKES,key,defaultVale);
	}

	public static long getLongProperty(String key, long defValue) {
		long v = defValue;
		String value = getProperty(key);
		if (value != null) {
			try {
				v = Long.valueOf(value.trim());
			} catch (Exception e) {
			}
		}
		return v;
	}
}
