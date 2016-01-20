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
package org.jmetis.kernel.factory;

import java.security.PrivilegedExceptionAction;

/**
 * {@code PrivilegedObjectFactory}
 * 
 * @author era
 */
class PrivilegedObjectFactory<T> implements PrivilegedExceptionAction<T> {

	private final ObjectFactory<T> objectFactory;

	/**
	 * Constructs a new {@code PrivilegedObjectFactory} instance.
	 * 
	 */
	public PrivilegedObjectFactory(ObjectFactory<T> objectFactory) {
		super();
		this.objectFactory = objectFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.PrivilegedExceptionAction#run()
	 */
	public T run() throws Exception {
		return this.objectFactory.constructor.newInstance();
	}

}
