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
package org.jmetis.kernel.context;

import java.util.HashMap;
import java.util.Map;

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.kernel.service.IServiceRegistry;

/**
 * {@code EvaluationContext}
 * 
 * @author aerlach
 */
public class EvaluationContext implements IEvaluationContext {

	private IEvaluationContext parentContext;

	private Map<String, Object> attributes;

	private IServiceRegistry serviceRegistry;

	/**
	 * Constructs a new {@code EvaluationContext} instance.
	 * 
	 * @param parentContext
	 */
	public EvaluationContext(IEvaluationContext parentContext) {
		super();
		this.parentContext = Assertions.mustNotBeNull("parentContext",
				parentContext);
	}

	/**
	 * Constructs a new {@code EvaluationContext} instance.
	 */
	public EvaluationContext() {
		super();
		this.serviceRegistry = this.locateServiceRegistry();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.context.IEvaluationContext#getAttribute(java.lang.String
	 * )
	 */
	public Object getAttribute(String name) {
		if (this.attributes != null) {
			Object value = this.attributes.get(name);
			if (value != null) {
				return value;
			}
		}
		if (this.parentContext != null) {
			return this.parentContext.getAttribute(name);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.context.IEvaluationContext#getAttribute(java.lang.String
	 * , java.lang.Class)
	 */
	public <T> T getAttribute(String name, Class<T> expectedType) {
		return expectedType.cast(this.getAttribute(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.context.IEvaluationContext#setAttribute(java.lang.String
	 * , java.lang.Object)
	 */
	public void setAttribute(String name, Object value) {
		if (this.attributes == null) {
			this.attributes = new HashMap<String, Object>();
		}
		this.attributes.put(name, value);
	}

	protected IServiceRegistry locateServiceRegistry() {
		return IServiceRegistry.EMPTY_SERVICE_REGISTRY;
	}

	protected IServiceRegistry serviceRegistry() {
		return this.serviceRegistry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.IServiceRegistry#acquireService(java.lang.Class,
	 * java.lang.String)
	 */
	public <T> T acquireService(Class<T> serviceType, String searchFilter) {
		return this.serviceRegistry().acquireService(serviceType, searchFilter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.IServiceRegistry#acquireService(java.lang.Class)
	 */
	public <T> T acquireService(Class<T> serviceType) {
		return this.serviceRegistry().acquireService(serviceType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.IServiceRegistry#releaseService(java.lang.Object)
	 */
	public void releaseService(Object service) {
		this.serviceRegistry().releaseService(service);
	}

}
