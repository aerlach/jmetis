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
 * {@code IServiceRegistry} provides an implementation independent interface for
 * service lookup.
 * 
 * @author aerlach
 */
public interface IServiceRegistry {

	IServiceRegistry EMPTY_SERVICE_REGISTRY = new EmptyServiceRegistry();

	/**
	 * 
	 * @param searchFilter
	 * @param serviceType
	 * @param <T>
	 * @return
	 */
	<T> T acquireService(Class<T> serviceType, String searchFilter);

	/**
	 * Returns the service corresponding to the given {@code serviceType}.
	 * 
	 * @param serviceType
	 *            the interface that the service implements; must not be {@code
	 *            null}
	 * @return the service corresponding to the given {@code serviceType}, or
	 *         {@code null} if no such service could be found.
	 */
	<T> T acquireService(Class<T> serviceType);

	/**
	 * 
	 * @param service
	 */
	void releaseService(Object service);

}
