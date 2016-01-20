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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.servlet.ServletContext;

/**
 * {@code ServicePublisher}
 * 
 * @author era
 */
public abstract class ServicePublisher {

	public static final String HOST_ADDRESS_NAME = "host-address";

	public static final String HOST_PORT_NAME = "host-port";

	public static final String SERVICE_TYPE_NAME = "service-type";

	public static final String DEFAULT_SERVICE_TYPE = "_http._tcp.local.";

	protected ServletContext servletContext;

	private InetAddress hostAddress;

	private int hostPort;

	private String serviceType;

	private JmDNS jmdns;

	private List<ServiceInfo> serviceInfos;

	/**
	 * Constructs a new {@code ServicePublisher} instance.
	 * 
	 */
	protected ServicePublisher() {
		super();
	}

	/**
	 * Writes the given {@code message} to a the log file.
	 * 
	 * @param message
	 *            the message to be written to the log file
	 */
	protected void logMessage(String message) {
		this.servletContext.log(message);
	}

	/**
	 * Writes an explanatory message and a stack trace for a given
	 * {@link Throwable exception} to the log file.
	 * 
	 * @param message
	 *            describes the error or exception
	 * @param exception
	 *            the error or exception
	 * 
	 */

	protected void logMessage(String message, Throwable exception) {
		this.servletContext.log(message, exception);
	}

	protected String getParameterValue(String name, String defaultValue) {
		String value = this.servletContext.getInitParameter(name);
		if (value != null) {
			return value;
		}
		return defaultValue;
	}

	protected InetAddress retrieveHostAddress() {
		String hostAddress = this.getParameterValue(
				ServicePublisher.HOST_ADDRESS_NAME, null);
		if (hostAddress != null) {
			try {
				return InetAddress.getByName(hostAddress);
			} catch (UnknownHostException ex) {
				this.logMessage(ex.getMessage(), ex);
			}
		}
		return null;
	}

	protected InetAddress getHostAddress() {
		if (this.hostAddress == null) {
			this.hostAddress = this.retrieveHostAddress();
		}
		return this.hostAddress;
	}

	protected int retrieveHostPort() {
		String hostPort = this.getParameterValue(
				ServicePublisher.HOST_PORT_NAME, null);
		if (hostPort != null) {
			try {
				return Integer.parseInt(hostPort);
			} catch (NumberFormatException ex) {
				this.logMessage(ex.getMessage(), ex);
			}
		}
		return 0;
	}

	protected int getHostPort() {
		if (this.hostPort == 0) {
			this.hostPort = this.retrieveHostPort();
		}
		return this.hostPort;
	}

	protected String getServiceType() {
		if (this.serviceType == null) {
			this.serviceType = this.getParameterValue(
					ServicePublisher.SERVICE_TYPE_NAME,
					ServicePublisher.DEFAULT_SERVICE_TYPE);
		}
		return this.serviceType;
	}

	protected void publishService(String name, Map<String, String> properties) {
		try {
			if (this.jmdns == null) {
				InetAddress hostAddress = this.getHostAddress();
				if (hostAddress == null) {
					this.jmdns = JmDNS.create();
				} else {
					this.jmdns = JmDNS.create(hostAddress);
				}
			}
			ServiceInfo serviceInfo = ServiceInfo.create(this.getServiceType(),
					name + "@" + this.getHostAddress(), this.getHostPort(), 0,
					0, new Hashtable<String, String>(properties));
			this.jmdns.registerService(serviceInfo);
			if (this.serviceInfos == null) {
				this.serviceInfos = new ArrayList<ServiceInfo>();
			}
			this.serviceInfos.add(serviceInfo);
		} catch (IOException ex) {
			this.logMessage(ex.getMessage(), ex);
		}

	}

	protected void revokeServices() {
		if (this.serviceInfos != null) {
			for (ServiceInfo serviceInfo : this.serviceInfos) {
				this.jmdns.unregisterService(serviceInfo);
			}
		}
	}

}
