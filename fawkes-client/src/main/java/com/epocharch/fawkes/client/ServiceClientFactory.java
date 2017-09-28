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

package com.epocharch.fawkes.client;

import com.epocharch.fawkes.client.actor.MethodActorRepository;
import com.epocharch.fawkes.common.meta.AppMeta;
import com.epocharch.fawkes.common.meta.ClientMeta;
import com.epocharch.fawkes.common.meta.ServiceMeta;
import org.springframework.beans.factory.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by archer on 13/09/2017.
 */
public class ServiceClientFactory implements FactoryBean,InitializingBean,DisposableBean {

	private AppMeta appMeta;
	private Object serviceProxy;
	private String serviceAppName;
	private String orgnization;
	private String serviceName;
	private String serviceVersion;
	private String groupName;
	private long timeout;
	private boolean autoRedo = false;
	private Class serviceInterface;
	private String balanceAlgo = ClientConstant.BALANCER_WEIGHTED_ROUNDROBIN;
	private ClientMeta clientMeta;
	private ServiceMeta serviceMeta;
	/**
	 * Invoked by a BeanFactory on destruction of a singleton.
	 *
	 * @throws Exception in case of shutdown errors.
	 *                   Exceptions will get logged but not rethrown to allow
	 *                   other beans to release their resources too.
	 */
	public void destroy() throws Exception {

	}

	/**
	 * Return an instance (possibly shared or independent) of the object
	 * managed by this factory.
	 * <p>As with a {@link BeanFactory}, this allows support for both the
	 * Singleton and Prototype design pattern.
	 * <p>If this FactoryBean is not fully initialized yet at the time of
	 * the call (for example because it is involved in a circular reference),
	 * throw a corresponding {@link FactoryBeanNotInitializedException}.
	 * <p>As of Spring 2.0, FactoryBeans are allowed to return {@code null}
	 * objects. The factory will consider this as normal value to be used; it
	 * will not throw a FactoryBeanNotInitializedException in this case anymore.
	 * FactoryBean implementations are encouraged to throw
	 * FactoryBeanNotInitializedException themselves now, as appropriate.
	 *
	 * @return an instance of the bean (can be {@code null})
	 * @throws Exception in case of creation errors
	 * @see FactoryBeanNotInitializedException
	 */
	public Object getObject() throws Exception {

		return this.serviceProxy;
	}

	/**
	 * Return the type of object that this FactoryBean creates,
	 * or {@code null} if not known in advance.
	 * <p>This allows one to check for specific types of beans without
	 * instantiating objects, for example on autowiring.
	 * <p>In the case of implementations that are creating a singleton object,
	 * this method should try to avoid singleton creation as far as possible;
	 * it should rather estimate the type in advance.
	 * For prototypes, returning a meaningful type here is advisable too.
	 * <p>This method can be called <i>before</i> this FactoryBean has
	 * been fully initialized. It must not rely on state created during
	 * initialization; of course, it can still use such state if available.
	 * <p><b>NOTE:</b> Autowiring will simply ignore FactoryBeans that return
	 * {@code null} here. Therefore it is highly recommended to implement
	 * this method properly, using the current state of the FactoryBean.
	 *
	 * @return the type of object that this FactoryBean creates,
	 * or {@code null} if not known at the time of the call
	 * @see ListableBeanFactory#getBeansOfType
	 */
	public Class<?> getObjectType() {
		return serviceInterface;
	}

	/**
	 * Is the object managed by this factory a singleton? That is,
	 * will {@link #getObject()} always return the same object
	 * (a reference that can be cached)?
	 * <p><b>NOTE:</b> If a FactoryBean indicates to hold a singleton object,
	 * the object returned from {@code getObject()} might get cached
	 * by the owning BeanFactory. Hence, do not return {@code true}
	 * unless the FactoryBean always exposes the same reference.
	 * <p>The singleton status of the FactoryBean itself will generally
	 * be provided by the owning BeanFactory; usually, it has to be
	 * defined as singleton there.
	 * <p><b>NOTE:</b> This method returning {@code false} does not
	 * necessarily indicate that returned objects are independent instances.
	 * An implementation of the extended {@link SmartFactoryBean} interface
	 * may explicitly indicate independent instances through its
	 * {@link SmartFactoryBean#isPrototype()} method. Plain {@link FactoryBean}
	 * implementations which do not implement this extended interface are
	 * simply assumed to always return independent instances if the
	 * {@code isSingleton()} implementation returns {@code false}.
	 *
	 * @return whether the exposed object is a singleton
	 * @see #getObject()
	 * @see SmartFactoryBean#isPrototype()
	 */
	public boolean isSingleton() {
		return false;
	}

	/**
	 * Invoked by a BeanFactory after it has set all bean properties supplied
	 * (and satisfied BeanFactoryAware and ApplicationContextAware).
	 * <p>This method allows the bean instance to perform initialization only
	 * possible when all bean properties have been set and to throw an
	 * exception in the event of misconfiguration.
	 *
	 * @throws Exception in the event of misconfiguration (such
	 *                   as failure to set an essential property) or if initialization fails.
	 */
	public void afterPropertiesSet() throws Exception {
		AppMeta serverApp = new AppMeta(serviceAppName);
		ClientMeta clientMeta = new ClientMeta(appMeta);

		serviceMeta = new ServiceMeta(serverApp,clientMeta);
		serviceMeta.setServiceName(serviceName);
		serviceMeta.setServiceVersion(serviceVersion);
		serviceMeta.setAutoRedo(autoRedo);
		serviceMeta.setGroupName(groupName);
		MethodActorRepository.getInstance().deploy(serviceInterface,serviceMeta);
		InvocationHandler handler = new RequestHandler(serviceMeta);
		serviceProxy = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { serviceInterface }, handler);
	}
}
