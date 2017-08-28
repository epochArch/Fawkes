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

package com.epocharch.fawkes.common.context;

import com.epocharch.fawkes.common.constants.Constants;
import com.epocharch.fawkes.common.constants.PropKeys;
import com.epocharch.fawkes.common.uuid.KeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class FawkesContextUtil {
	private static Logger logger = LoggerFactory.getLogger(FawkesContextUtil.class);
	private static ThreadLocal<InvocationContext> invocationContext = new ThreadLocal<InvocationContext>();

	public static InvocationContext getInvocationContext() {
		InvocationContext ic = invocationContext.get();
		if (ic == null) {
			ic = new InvocationContext();
			invocationContext.set(ic);
		}
		return ic;
	}

	public static void setInvocationContext(InvocationContext context) {
		if (context != null) {
			invocationContext.set(context);
		}
	}

	public static String getGlobalId() {
		String id = getString(Constants.GLOBAL_ID, "");
		if (StringUtils.isBlank(id)) {
			String appId = RootContainer.getInstance().getAppId();
			if (StringUtils.isBlank(appId)) {
				appId = System.getProperty(PropKeys.APP_ID, "unknownApp");
			}
			id = KeyUtil.getGlobalId(appId);
			setAttribute(Constants.GLOBAL_ID, id);
		}
		return id;
	}

	public static void setGlobalId(String globalId) {
		if (!StringUtils.isBlank(globalId)) {
			setAttribute(Constants.GLOBAL_ID, globalId);
		}
	}

	public static FawkesGlobalIdVo getGlobalIdVo() {
		FawkesGlobalIdVo resultVo = new FawkesGlobalIdVo();
		String id = getString(Constants.GLOBAL_ID, "");
		if (StringUtils.isBlank(id)) {
			String appName = RootContainer.getInstance().getAppId();
			if (StringUtils.isBlank(appName)) {
				appName = System.getProperty("clientAppName", "unknownApp");
			}
			id = KeyUtil.getGlobalId(appName);
			setAttribute(Constants.GLOBAL_ID, id);
			resultVo.setNewCreated(true);
		}
		resultVo.setGlobalId(id);
		return resultVo;
	}

	public static String getRequestId() {
		return getString(Constants.REQUEST_ID, "0");

	}

	public static void setRequestId(String requestId) {
		if (!StringUtils.isBlank(requestId)) {
			setAttribute(Constants.REQUEST_ID, requestId);
		}
	}

	public static int getRequestHop() {
		return getInvocationContext().getHopValue();
	}

	public static boolean isVoidMethod() {
		String isVoid = getString(Constants.METHOD_IS_VOID_KEY, "0");
		if (isVoid != null && isVoid.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	public static void setVoidMethod(boolean b) {
		if (b) {
			setAttribute(Constants.METHOD_IS_VOID_KEY, "1");
		} else {
			setAttribute(Constants.METHOD_IS_VOID_KEY, "0");
		}
	}

	public static Object[] getArguments() {
		Object obj = getAttribute(Constants.METHOD_ARGUMENTS_KEY, null);
		if (obj != null && obj instanceof Object[]) {
			return (Object[]) obj;
		} else {
			return null;
		}
	}

	public static void setArguments(Object[] params) {
		if (params != null) {
			setAttribute(Constants.METHOD_ARGUMENTS_KEY, params);
		}
	}

	public static String getTransactionId() {
		return getString(Constants.TXN_ID, "");
	}

	public static void setTransactionId(String txnId) {
		if (!StringUtils.isBlank(txnId)) {
			setAttribute(Constants.TXN_ID, txnId);
		}
	}

	public static void setAttribute(String key, Object value) {
		getInvocationContext().putValue(key, value);
	}

	public static Object getAttribute(String key, Object defValue) {
		Object value = defValue;
		try {
			value = getInvocationContext().getValue(key, defValue);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return value;
	}

	public static String getString(String key, String defValue) {
		String v = defValue;
		try {
			v = getInvocationContext().getStrValue(key, defValue);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return v;
	}

	@Deprecated
	public static void clean() {
		cleanGlobal();
	}

	// 清除内部异步调用线程变量
	@Deprecated
	private static void cleanLocal() {
		getInvocationContext().cleanLocalContext();
	}

	// 清除外部调用线程变量，比如Web Action
	public static void cleanGlobal() {
		getInvocationContext().cleanGlobalContext();
	}

	// 根据globalId生成情况来清除线程变量(谁生成谁清理原则）
	public static void cleanGlobal(FawkesGlobalIdVo globalIdVo) {
		if (globalIdVo != null) {
			if (globalIdVo.isNewCreated()) {
				getInvocationContext().cleanGlobalContext();
			} else {
				getInvocationContext().cleanLocalContext();
			}
		}
	}
}
