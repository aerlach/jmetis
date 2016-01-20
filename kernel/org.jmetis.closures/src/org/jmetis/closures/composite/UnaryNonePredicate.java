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
package org.jmetis.closures.composite;

import org.jmetis.kernel.closure.IUnaryPredicate;

/**
 * {@code UnaryNonePredicate} evaluates each given {@link IUnaryPredicate} in
 * sequence until either any single predicate returns {@code true} (and {@code
 * false} is returned), or the {@code UnaryAnyPredicate} is exhausted (and
 * {@code true} is returned).
 * 
 * @author era
 */
public class UnaryNonePredicate<A> extends UnaryPredicateComposite<A> {

	/**
	 * Constructs a new {@code UnaryNonePredicate} instance with the given
	 * {@code components}.
	 * 
	 * @param components
	 *            the predicates to evaluate in sequence
	 */
	public UnaryNonePredicate(IUnaryPredicate<? super A>... components) {
		super(components);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.closures.IUnaryPredicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(A argument) {
		for (IUnaryPredicate<? super A> p : this.components) {
			if (p.evaluate(argument)) {
				return false;
			}
		}
		return true;
	}

}
