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
 * @author era
 * 
 */
public abstract class UnaryPredicateComposite<A> implements IUnaryPredicate<A> {

	protected final IUnaryPredicate<? super A>[] components;

	/**
	 * Constructs a new {@code UnaryPredicateComposite} instance with the given
	 * {@code components}.
	 * 
	 * @param components
	 *            the predicates to evaluate in sequence
	 */
	protected UnaryPredicateComposite(
			IUnaryPredicate<? super A>... components) {
		super();
		this.components = components;
	}

}
