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
 * {@code IsNotNullPredicate} returns {@code true} if the argument is not {@code null}.
 * 
 * @author era
 * 
 */
public class IsNotNullPredicate<A> implements IUnaryPredicate<A> {

	@SuppressWarnings("unchecked")
	private static final IUnaryPredicate INSTANCE = new IsNotNullPredicate();

	/**
	 * Restricted constructor.
	 */
	private IsNotNullPredicate() {
		super();
	}

	@SuppressWarnings("unchecked")
	public static <T> IUnaryPredicate<T> getInstance() {
		return IsNotNullPredicate.INSTANCE;
	}

	/**
	 * Evaluates the predicate returning true if the input is not null.
	 * 
	 * @param object
	 *            the input object
	 * @return true if input is not null
	 * 
	 * @see org.jmetis.kernel.closure.IUnaryPredicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(A argument) {
		return argument != null;
	}

}
