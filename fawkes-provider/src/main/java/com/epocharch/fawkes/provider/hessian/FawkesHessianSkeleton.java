
/*
 * Copyright 2017 EpochArch.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epocharch.fawkes.provider.hessian;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.services.server.AbstractSkeleton;
import com.caucho.services.server.ServiceContext;
import com.epocharch.fawkes.common.constants.Constants;
import com.epocharch.fawkes.common.context.FawkesContextUtil;
import com.epocharch.fawkes.common.exception.FawkesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author Archer
 * 
 */
public class FawkesHessianSkeleton extends AbstractSkeleton {
	private static final Logger log = LoggerFactory.getLogger(FawkesHessianSkeleton.class);

	private Object _service;

	private TpsThresholdChecker ttc;

	private int tpsThreshold;

	/**
	 * Create a new hessian skeleton.
	 * 
	 * @param service
	 *            the underlying service object.
	 * @param apiClass
	 *            the API interface
	 */
	public FawkesHessianSkeleton(Object service, Class apiClass, int tpsThreshold) {
		super(apiClass);
		this.tpsThreshold = tpsThreshold;
		if (service == null)
			service = this;

		_service = service;

		if (!apiClass.isAssignableFrom(service.getClass())) {
			throw new IllegalArgumentException("Service " + service + " must be an instance of " + apiClass.getName());
		}
		ttc = new TpsThresholdChecker(tpsThreshold);
	}

	/**
	 * Invoke the object with the request from the input stream.
	 * 
	 * @param in
	 *            the Hessian input stream
	 * @param out
	 *            the Hessian output stream
	 */
	public void invoke(AbstractHessianInput in, AbstractHessianOutput out) throws Throwable {
		ServiceContext context = ServiceContext.getContext();

		// backward compatibility for some frameworks that don't read
		// the call type first
		in.skipOptionalCall();

		String header;
		while ((header = in.readHeader()) != null) {
			Object value = in.readObject();
			if (context != null) {
				context.addHeader(header, value);
			}
			if (value != null) {
				FawkesContextUtil.setAttribute(header, value);
			}
		}
		String methodName = in.readMethod();
		final Method method = getMethod(methodName);

		if (method != null) {
		} else if ("_hessian_getAttribute".equals(methodName)) {
			String attrName = in.readString();
			in.completeCall();

			String value = null;

			if ("java.api.class".equals(attrName))
				value = getAPIClassName();
			else if ("java.home.class".equals(attrName))
				value = getHomeClassName();
			else if ("java.object.class".equals(attrName))
				value = getObjectClassName();

			out.startReply();

			out.writeObject(value);

			out.completeReply();

			return;
		} else {
			out.startReply();
			out.writeFault("NoSuchMethodException", "The service has no method named: " + in.getMethod(), null);
			out.completeReply();
			return;
		}

		Class[] args = method.getParameterTypes();
		final Object[] values = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			values[i] = in.readObject(args[i]);
		}

		Object result = null;
		try {
			FawkesContextUtil.setArguments(values);
			if (ttc.isReached()) {
				throw new FawkesException("Exceed service capacity, tpsThreshold:" + tpsThreshold);
			}

			/*
			 * Updated by Frank Wang on Dec 7th, 2016
			 * 
			 * Prepare parent id for the next SOA invocation.
			 */
			String parentId = FawkesContextUtil.getRequestId();
			FawkesContextUtil.getInvocationContext().putValue(Constants.REQUEST_PARENT_ID,parentId);
			
			result = method.invoke(_service, values);
			out.startReply();
			out.writeObject(result);
			out.completeReply();
		} catch (Throwable e) {
			if (e instanceof InvocationTargetException)
				e = ((InvocationTargetException) e).getTargetException();
			log.error(e.getMessage(), e);
			out.startReply();
			try {
				out.writeFault("ServiceException", e.getMessage(), e);
			} catch (Throwable e2) {
				log.error(e2.getMessage(), e2);
			}
			out.completeReply();
			throw e;
		} finally {
			// The complete call needs to be after the invoke to handle a
			// trailing InputStream
			in.completeCall();

			out.close();
		}

	}
}
