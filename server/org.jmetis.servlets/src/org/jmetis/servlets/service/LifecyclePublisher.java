/*
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
package org.jmetis.servlets.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Wrapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 * {@code LifecyclePublisher} can be registered as {@link LifecycleListener} for
 * Apache Tomcat 5 & 6:
 * <ul>
 * <li>in the $CATALINA_BASE/conf/context.xml file: the Context element
 * information will be loaded by all webapps</li>
 * <li>in the $CATALINA_BASE/conf/[enginename]/[hostname]/context.xml.default
 * file: the Context element information will be loaded by all webapps of that
 * host</li>
 * <li>in individual files (with a ".xml" extension) in the
 * $CATALINA_BASE/conf/[enginename]/[hostname]/ directory. The name of the file
 * (less the .xml extension) will be used as the context path. Multi-level
 * context paths may be defined using #, e.g. context#path.xml. The default web
 * application may be defined by using a file called ROOT.xml</li>
 * <li>if the previous file was not found for this application, in an individual
 * file at /META-INF/context.xml inside the application files</li>
 * <li>inside a Host element in the main conf/server.xml</li>
 * <ul>
 * 
 * @author era
 * @see LifecycleListener
 */
public class LifecyclePublisher extends ServicePublisher implements
		LifecycleListener {

	protected static final Log LOGGER = LogFactory
			.getLog(LifecyclePublisher.class);

	/**
	 * Constructs a new {@code LifecyclePublisher} instance.
	 * 
	 */
	public LifecyclePublisher() {
		super();
	}

	protected String usageMessage() {
		return this.getClass().getName()
				+ " can be registered as lifecycle listener:\n\tin the $CATALINA_BASE/conf/context.xml file: the Context element information will be loaded by all webapps\n\tin the $CATALINA_BASE/conf/[enginename]/[hostname]/context.xml.default file: the Context element information will be loaded by all webapps of that host\n\tin individual files (with a \".xml\" extension) in the $CATALINA_BASE/conf/[enginename]/[hostname]/ directory. The name of the file (less the .xml extension) will be used as the context path. Multi-level context paths may be defined using #, e.g. context#path.xml. The default web application may be defined by using a file called ROOT.xml.\n\tif the previous file was not found for this application, in an individual file at /META-INF/context.xml inside the application files\n\tinside a Host element in the main conf/server.xml";
	}

	protected void contextInitialized(Context context) {
		this.servletContext = context.getServletContext();
		String contextPath = this.servletContext.getContextPath();
		for (Container child : context.findChildren()) {
			if (child instanceof Wrapper) {
				Wrapper wrapper = (Wrapper) child;
				String serviceAPI = wrapper.findInitParameter("home-api");
				if (serviceAPI != null) {
					for (String servletMapping : wrapper.findMappings()) {
						String servicePath = contextPath + servletMapping;
						Map<String, String> properties = new HashMap<String, String>();
						properties.put("path", servicePath);
						properties.put("api", serviceAPI);
						this.publishService(wrapper.getName(), properties);
					}
				}
			}
		}
	}

	protected void contextDestroyed(Context context) {
		this.revokeServices();
		this.servletContext = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.catalina.LifecycleListener#lifecycleEvent(org.apache.catalina
	 * .LifecycleEvent)
	 */
	public void lifecycleEvent(LifecycleEvent lifecycleEvent) {
		Lifecycle lifecycle = lifecycleEvent.getLifecycle();
		if (lifecycle instanceof Context) {
			Context context = (Context) lifecycle;
			this.servletContext = context.getServletContext();
			String eventType = lifecycleEvent.getType();
			if (Lifecycle.START_EVENT.equals(eventType)) {
				this.contextInitialized(context);
			} else if (Lifecycle.STOP_EVENT.equals(eventType)) {
				this.contextDestroyed(context);
			}
		} else {
			LifecyclePublisher.LOGGER.warn(this.usageMessage());
		}
	}

}
