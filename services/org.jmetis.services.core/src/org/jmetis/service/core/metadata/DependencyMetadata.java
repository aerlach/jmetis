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
package org.jmetis.service.core.metadata;

/**
 * Metadata of a dependency
 * 
 * @author <a href="mailto:dev@felix.apache.org">Felix Project Team</a>
 */
public class DependencyMetadata {
	private String m_serviceName = "";
	private String m_filter = "";
	private String m_bindMethod = "";
	private String m_unbindMethod = "";
	private String m_cardinality = "";
	private String m_policy = "";

	private boolean m_isStatic = true;
	private boolean m_isOptional = false;
	private boolean m_isMultiple = false;

	/**
	 * Constructor
	 * 
	 **/
	DependencyMetadata(String servicename, String cardinality, String policy,
			String filter, String bindmethod, String unbindmethod) {
		this.m_serviceName = servicename;

		String classnamefilter = "(objectClass=" + servicename + ")";

		if (filter.equals("") == false) {
			this.m_filter = "(&" + classnamefilter + filter + ")";
		} else {
			this.m_filter = classnamefilter;
		}

		this.m_bindMethod = bindmethod;
		this.m_unbindMethod = unbindmethod;
		this.m_cardinality = cardinality;
		this.m_policy = policy;

		if (policy.equals("static") == false) {
			this.m_isStatic = false;
		}

		if (cardinality.equals("0..1") || cardinality.equals("0..n")) {
			this.m_isOptional = true;
		}

		if (cardinality.equals("0..n") || cardinality.equals("1..n")) {
			this.m_isMultiple = true;
		}
	}

	/**
	 * Returns the name of the required service
	 * 
	 * @return the name of the required service
	 **/
	public String getServiceName() {
		return this.m_serviceName;
	}

	/**
	 * Returns the filter
	 * 
	 * @return A string with the filter
	 **/
	public String getFilter() {
		return this.m_filter;
	}

	/**
	 * Get the name of the Bind method
	 * 
	 * @return a String with the name of the BindMethod
	 **/
	public String getBindMethodName() {
		return this.m_bindMethod;
	}

	/**
	 * Get the name of the Unbind method
	 * 
	 * @return a String with the name of the Unbind method
	 **/
	public String getUnbindMethodName() {
		return this.m_unbindMethod;
	}

	/**
	 * Test if dependency's binding policy is static
	 * 
	 * @return true if static
	 **/
	public boolean isStatic() {
		return this.m_isStatic;
	}

	/**
	 * Test if dependency is optional (0..1 or 0..n)
	 * 
	 * @return true if the dependency is optional
	 **/
	public boolean isOptional() {
		return this.m_isOptional;
	}

	/**
	 * Test if dependency is multiple (0..n or 1..n)
	 * 
	 * @return true if the dependency is multiple
	 **/
	public boolean isMultiple() {
		return this.m_isMultiple;
	}

	/**
	 * Get the cardinality as a string
	 * 
	 * @return the cardinality
	 **/
	public String getCardinality() {
		return this.m_cardinality;
	}

	/**
	 * Get the policy as a string
	 * 
	 * @return the policy
	 **/
	public String getPolicy() {
		return this.m_policy;
	}
}
