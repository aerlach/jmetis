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

import org.jmetis.closures.composite.BinaryAndPredicate;
import org.jmetis.closures.composite.BinaryOrPredicate;
import org.jmetis.closures.decorator.BinaryNotPredicate;
import org.jmetis.kernel.closure.IBinaryPredicate;

/**
 * @author era
 * 
 */
public abstract class BinaryPredicate<A1, A2> implements
IBinaryPredicate<A1, A2> {

	/**
	 * Constructs a new {@code BinaryPredicate} instance.
	 */
	protected BinaryPredicate() {
		super();
	}

	/**
	 * Answers a predicate that represents the logical <em>conjunction</em> of
	 * the receiver and another predicate. The conjunction's
	 * {@link #evaluate(Object)} method answers {@code true} if both this
	 * predicate and the other predicate would answer {@code true}. <p/> When
	 * the conjunction is evaluated, this predicate is always evaluated first.
	 * The other predicate may not be evaluated; so do not depend on its
	 * evaluation for side effects.
	 * 
	 * @param otherPredicate
	 *            the "right-hand" operand of the conjunction
	 * @return the logical conjunction of this predicate and {@code other}
	 * @throws NullPointerException
	 *             if {@code otherPredicate} is {@code null}
	 */
	public IBinaryPredicate<A1, A2> and(
			IBinaryPredicate<? super A1, ? super A2> otherPredicate) {
		return new BinaryAndPredicate<A1, A2>(this, otherPredicate);
	}

	// public IBinaryPredicate<A1, A2> and(IUnaryPredicate<? super A1>
	// otherPredicate) {
	// return null;
	// }
	//
	// public IBinaryPredicate<A1, A2> and(IUnaryPredicate<? super A2>
	// otherPredicate) {
	// return null;
	// }

	/**
	 * Answers a predicate that represents the logical <em>inverse</em> of the
	 * receiver; wherever the receiver's {@link #evaluate(Object) matches}
	 * method would answer {@code true}, the inverse answers {@code false}; and
	 * vice versa.
	 * 
	 * @return the inverse of this predicate
	 */
	public IBinaryPredicate<A1, A2> not() {
		return new BinaryNotPredicate<A1, A2>(this);
	}

	/**
	 * Answers a predicate that represents the logical <em>disjunction</em> of
	 * the receiver and another predicate. The disjunction's
	 * {@link #evaluate(Object)} method answers {@code true} if either this
	 * predicate or the other predicate would answer {@code true}. <p/> When the
	 * disjunction is evaluated, this predicate is always evaluated first. The
	 * other predicate may not be evaluated; so do not depend on its evaluation
	 * for side effects.
	 * 
	 * @param otherPredicate
	 *            the "right-hand" operand of the disjunction
	 * @return the logical disjunction of this predicate and {@code other}
	 * @throws NullPointerException
	 *             if {@code otherPredicate} is {@code null}
	 */
	public IBinaryPredicate<A1, A2> or(
			IBinaryPredicate<? super A1, ? super A2> otherPredicate) {
		return new BinaryOrPredicate<A1, A2>(this, otherPredicate);
	}

}
