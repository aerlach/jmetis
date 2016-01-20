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

import org.jmetis.kernel.service.IServiceRegistry;

/**
 * {@code IEvaluationContext}
 * 
 * @author aerlach
 */
public interface IEvaluationContext extends IServiceRegistry {

	/**
	 * @param name
	 * @return
	 */
	Object getAttribute(String name);

	/**
	 * @param <T>
	 * @param name
	 * @param expectedType
	 * @return
	 */
	<T> T getAttribute(String name, Class<T> expectedType);

	/**
	 * @param name
	 * @param value
	 */
	void setAttribute(String name, Object value);

}
