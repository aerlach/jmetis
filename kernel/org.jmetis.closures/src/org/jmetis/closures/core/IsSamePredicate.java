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
 * {@code IsSamePredicate} returns true if the input is the same object as the
 * one stored in this predicate.
 * 
 * @author era
 */
public class IsSamePredicate<A> implements IUnaryPredicate<A> {

	private final A value;

	/**
	 * Constructor that performs no validation.
	 * 
	 * @param value
	 *            the object to compare to
	 */
	public IsSamePredicate(A value) {
		super();
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.closures.IUnaryPredicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(A argument) {
		return this.value == argument;
	}

}
