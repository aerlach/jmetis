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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * {@code ServletContextPublisher}
 * 
 * @author era
 */
public class ServletContextPublisher extends ServicePublisher implements
		ServletContextListener {

	/**
	 * Constructs a new {@code ServletContextPublisher} instance.
	 * 
	 */
	public ServletContextPublisher() {
		super();
	}

	protected Element getWebDescriptor() throws IOException,
			ParserConfigurationException, SAXException {
		URL resource = this.servletContext.getResource("/WEB-INF/web.xml");
		InputStream inputStream = resource.openStream();
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document document = documentBuilder.parse(inputStream);
			return document.getDocumentElement();
		} finally {
			inputStream.close();
		}
	}

	protected Map<String, String> getServiceInterfaces(Element webDescriptor) {
		List<Element> servletElements = Primitives.allChildElementsNamed(
				webDescriptor, "servlet");
		Map<String, String> serviceInterfaces = new HashMap<String, String>();
		for (Element element : servletElements) {
			Element servletNameElement = Primitives.firstChildElementNamed(
					element, "servlet-name");
			Element homeApiElement = null;
			List<Element> initParamElements = Primitives.allChildElementsNamed(
					element, "init-param");
			for (Element element2 : initParamElements) {
				Element paramNameElement = Primitives.firstChildElementNamed(
						element2, "param-name");
				if ("object-api".equals(Primitives
						.textValueOf(paramNameElement))) {
					homeApiElement = Primitives.firstChildElementNamed(
							element2, "param-value");
					break;
				}
			}
			if (homeApiElement != null) {
				serviceInterfaces.put(Primitives
						.textValueOf(servletNameElement), Primitives
						.textValueOf(homeApiElement));
			}
		}
		return serviceInterfaces;
	}

	protected Map<String, String> getServletMappings(Element webDescriptor) {
		List<Element> servletMappingElements = Primitives
				.allChildElementsNamed(webDescriptor, "servlet-mapping");
		Map<String, String> servletMappings = new HashMap<String, String>();
		for (Element element : servletMappingElements) {
			Element servletNameElement = Primitives.firstChildElementNamed(
					element, "servlet-name");
			Element urlPatternElement = Primitives.firstChildElementNamed(
					element, "url-pattern");
			servletMappings.put(Primitives.textValueOf(servletNameElement),
					Primitives.textValueOf(urlPatternElement));
		}
		return servletMappings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		this.servletContext = servletContextEvent.getServletContext();
		try {
			Element webDescriptor = this.getWebDescriptor();
			Map<String, String> serviceInterfaces = this
					.getServiceInterfaces(webDescriptor);
			Map<String, String> servletMappings = this
					.getServletMappings(webDescriptor);
			for (Map.Entry<String, String> service : serviceInterfaces
					.entrySet()) {
				String servicePath = this.servletContext.getContextPath()
						+ servletMappings.get(service.getKey());
				Hashtable<String, String> properties = new Hashtable<String, String>();
				properties.put("path", servicePath);
				properties.put("api", service.getValue());
				this.publishService(service.getKey(), properties);
			}
		} catch (Exception ex) {
			this.logMessage(ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		this.revokeServices();
		this.servletContext = null;
	}

}
