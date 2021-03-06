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
package org.jmetis.closures.core;

import org.jmetis.kernel.closure.IUnaryPredicate;

/**
 * @author era
 *
 */
public class IsInstanceofPredicate<A> implements IUnaryPredicate<A> {

	private final Class<?> expectedType;

	/**
	 * 
	 */
	public IsInstanceofPredicate(Class<?> expectedType) {
		super();
		this.expectedType = expectedType;
	}

	/* (non-Javadoc)
	 * @see org.jmetis.closures.IUnaryPredicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(A argument) {
		return this.expectedType.isInstance(argument);
	}

}
