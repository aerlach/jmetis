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

import java.util.HashSet;
import java.util.Set;

import org.jmetis.kernel.closure.IUnaryPredicate;

/**
 * @author era
 * 
 */
public class IsUniquePredicate<A> implements IUnaryPredicate<A> {

	private final Set<A> seenObjects;

	/**
	 * 
	 */
	public IsUniquePredicate() {
		super();
		this.seenObjects = new HashSet<A>();
	}

	/**
	 * Evaluates the predicate returning true if the input object hasn't been
	 * received yet.
	 * 
	 * @param object
	 *            the input object
	 * @return true if this is the first time the object is seen
	 * @see org.jmetis.kernel.closure.IUnaryPredicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(A argument) {
		return this.seenObjects.add(argument);
	}

}
