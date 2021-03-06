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
 * {@code IsNullPredicate} returns {@code true} if the argument is {@code null}.
 * 
 * @author era
 * 
 */
public class IsNullPredicate<A> implements IUnaryPredicate<A> {

	@SuppressWarnings("unchecked")
	private static final IUnaryPredicate INSTANCE = new IsNullPredicate();

	/**
	 * Restricted constructor.
	 */
	private IsNullPredicate() {
		super();
	}

	@SuppressWarnings("unchecked")
	public static <T> IUnaryPredicate<T> getInstance() {
		return IsNullPredicate.INSTANCE;
	}

	/**
	 * Evaluates the predicate returning true if the input is null.
	 * 
	 * @param object
	 *            the input object
	 * @return true if input is null
	 * 
	 * @see org.jmetis.kernel.closure.IUnaryPredicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(A argument) {
		return argument == null;
	}

}
