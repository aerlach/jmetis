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

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * {@code ServiceInfoFactory}
 * 
 * @author era
 */
@Deprecated
public class ServiceInfoFactory {

	private JmDNS jmdns;

	private List<ServiceInfo> serviceInfos;

	/**
	 * Constructs a new {@code ServiceInfoFactory} instance.
	 */
	public ServiceInfoFactory() {
		super();
	}

	protected InetAddress getInetAddress(String value)
			throws UnknownHostException {
		if (value != null) {
			return InetAddress.getByName(value);
		}
		return null;
	}

	protected int getPortNumber(String value) throws NumberFormatException {
		return Integer.parseInt(value);
	}

	protected String getPath(String value) throws NumberFormatException {
		if (value == null) {
			throw new IllegalArgumentException("null");
		}
		return value;
	}

	public void registerServiceInfos(ServletContext servletContext)
			throws Exception {
		String serviceHost = servletContext.getInitParameter("serviceHost");
		InetAddress serviceAddress = this.getInetAddress(serviceHost);
		int servicePort = this.getPortNumber(servletContext
				.getInitParameter("servicePort"));
		URL resource = servletContext.getResource("/WEB-INF/web.xml");
		InputStream inputStream = resource.openStream();
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document document = documentBuilder.parse(inputStream);
			Element webDescriptor = document.getDocumentElement();
			List<Element> servletElements = Primitives.allChildElementsNamed(
					webDescriptor, "servlet");
			List<Element> servletMappingElements = Primitives
					.allChildElementsNamed(webDescriptor, "servlet-mapping");
			Map<String, String> serviceInterfaces = new HashMap<String, String>();
			for (Element element : servletElements) {
				Element servletNameElement = Primitives.firstChildElementNamed(
						element, "servlet-name");
				Element homeApiElement = null;
				List<Element> initParamElements = Primitives
						.allChildElementsNamed(element, "init-param");
				for (Element element2 : initParamElements) {
					Element paramNameElement = Primitives
							.firstChildElementNamed(element2, "param-name");
					if ("home-api".equals(Primitives
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
			Map<String, String> servletMappings = new HashMap<String, String>();
			for (Element element : servletMappingElements) {
				Element servletNameElement = Primitives.firstChildElementNamed(
						element, "servlet-name");
				Element urlPatternElement = Primitives.firstChildElementNamed(
						element, "url-pattern");
				servletMappings.put(Primitives.textValueOf(servletNameElement),
						Primitives.textValueOf(urlPatternElement));
			}
			this.serviceInfos = new ArrayList<ServiceInfo>();
			for (Map.Entry<String, String> service : serviceInterfaces
					.entrySet()) {
				String serviceName = service.getKey() + "@" + serviceHost;
				String servicePath = servletContext.getContextPath()
						+ servletMappings.get(serviceName);
				Hashtable<String, String> properties = new Hashtable<String, String>();
				properties.put("path", servicePath);
				properties.put("api", service.getValue());
				this.serviceInfos.add(ServiceInfo.create("_http._tcp.local.",
						serviceName, servicePort, 0, 0, properties));
			}
			if (serviceAddress == null) {
				this.jmdns = JmDNS.create();
			} else {
				this.jmdns = JmDNS.create(serviceAddress);
			}
			for (ServiceInfo serviceInfo : this.serviceInfos) {
				this.jmdns.registerService(serviceInfo);
			}
		} finally {
			inputStream.close();
		}
	}

	public void unregisterServiceInfos() {
		if (this.jmdns != null) {
			for (ServiceInfo serviceInfo : this.serviceInfos) {
				this.jmdns.unregisterService(serviceInfo);
			}
		}
	}
}
