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
package com.epocharch.fawkes.common.hessian;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.HessianRemoteObject;
import com.epocharch.fawkes.common.utils.TimeoutUtil;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Archer
 * 
 */
public class FawkesHessianProxyFactory extends HessianProxyFactory {
	private String _huser;
	private String _hpassword;
	private String _hbasicAuth;

	/**
	 * Creates the URL connection.
	 */
	protected URLConnection openConnection(URL url) throws IOException {
		URLConnection conn = url.openConnection();

		conn.setDoOutput(true);
		int timeout = 1000;
		try {
			Long reqReadTimeout= TimeoutUtil.getRequestReadTimeout();
			if(reqReadTimeout!=null&&reqReadTimeout>0L){
				timeout=reqReadTimeout.intValue();
			}else {
				timeout = (int) super.getReadTimeout();
			}
		} catch (Exception e) {
		}
		if (timeout > 0) {
			conn.setReadTimeout(timeout);
			conn.setConnectTimeout(timeout);
		}
		conn.setRequestProperty("Content-Type", "x-application/hessian");
		try {
			if (_hbasicAuth != null) {
				conn.setRequestProperty("Authorization", _hbasicAuth);
			} else if (_huser != null && _hpassword != null) {
				_hbasicAuth = "Basic " + new String(Base64.encodeBase64((_huser + ":" + _hpassword).getBytes()));
				conn.setRequestProperty("Authorization", _hbasicAuth);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

	public Object create(Class api, String urlName, ClassLoader loader) throws MalformedURLException {
		if (api == null)
			throw new NullPointerException("api must not be null for HessianProxyFactory.create()");
		InvocationHandler handler = null;

		URL url = new URL(urlName);
		handler = new FawkesHessianProxy(this, url);

		return Proxy.newProxyInstance(loader, new Class[] { api, HessianRemoteObject.class }, handler);
	}

	public AbstractHessianOutput getHessianOutput(OutputStream os) {
		AbstractHessianOutput out = new FawkesHessianOutput(os);
		return out;
	}

	@Override
	public AbstractHessianInput getHessianInput(InputStream is) {
		AbstractHessianInput in = new FawkesHessianInput(is);
		return in;
	}

	@Override
	public void setPassword(String password) {
		super.setPassword(password);
		_hpassword = password;
	}

	@Override
	public void setUser(String user) {
		super.setUser(user);
		_huser = user;
	}
}
