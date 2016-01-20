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
package org.jmetis.service.core;

/**
 * {@code ServiceRegistry} implements the default behavior of the
 * {@link IServiceRegistry} interface.
 * 
 * @author aerlach
 */
public abstract class ServiceRegistry implements IServiceRegistry {

	/**
	 * Constructs a new {@code ServiceRegistry} instance.
	 */
	protected ServiceRegistry() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.service.IServiceRegistry#acquireService(java.lang.Class
	 * , java.lang.String)
	 */
	public <T> T acquireService(Class<T> serviceType, String searchFilter) {
		return this.acquireService(serviceType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.service.IServiceRegistry#releaseService(java.lang.Object
	 * )
	 */
	public void releaseService(Object service) {
		// intentionally do nothing
	}

}
