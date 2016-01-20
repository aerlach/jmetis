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

/**
 * {@code IObjectFactory} is a generic factory interface.
 * 
 * @author era
 */
public interface IObjectFactory<T> {

	/**
	 * Returns the class of the object to be instantiated without actually
	 * creating an instance.
	 * 
	 * @return the class of the object to be instantiated
	 */
	Class<T> getObjectType();

	/**
	 * Retrieves an instance of the object. This may create a new instance or
	 * look up an existing instance depending on the implementation. If a new
	 * instance is created it will also be initialized by this method
	 * 
	 * @param arguments
	 * @return
	 */
	T createInstance(Object... arguments);

}
