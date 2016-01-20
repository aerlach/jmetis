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

/**
 * {@code EvaluationContextDecorator}
 * 
 * @author aerlach
 */
public class EvaluationContextDecorator implements IEvaluationContext {

	private IEvaluationContext decoratedEvaluationContext;

	/**
	 * Constructs a new {@code EvaluationContextDecorator} instance.
	 */
	public EvaluationContextDecorator(
			IEvaluationContext decoratedEvaluationContext) {
		super();
		this.decoratedEvaluationContext = decoratedEvaluationContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.context.IEvaluationContext#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		return this.decoratedEvaluationContext.getAttribute(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.context.IEvaluationContext#getAttribute(java.lang.String,
	 * java.lang.Class)
	 */
	public <T> T getAttribute(String name, Class<T> expectedType) {
		return this.decoratedEvaluationContext.getAttribute(name, expectedType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.context.IEvaluationContext#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	public void setAttribute(String name, Object value) {
		this.decoratedEvaluationContext.setAttribute(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.IServiceRegistry#acquireService(java.lang.Class,
	 * java.lang.String)
	 */
	public <T> T acquireService(Class<T> serviceType, String searchFilter) {
		return this.decoratedEvaluationContext.acquireService(serviceType,
				searchFilter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.IServiceRegistry#acquireService(java.lang.Class)
	 */
	public <T> T acquireService(Class<T> serviceType) {
		return this.decoratedEvaluationContext.acquireService(serviceType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.IServiceRegistry#releaseService(java.lang.Object)
	 */
	public void releaseService(Object service) {
		this.decoratedEvaluationContext.releaseService(service);
	}

}
