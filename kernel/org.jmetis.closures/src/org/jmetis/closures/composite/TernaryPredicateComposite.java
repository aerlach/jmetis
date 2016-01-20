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

import org.jmetis.kernel.closure.ITernaryPredicate;

/**
 * @author era
 * 
 */
public abstract class TernaryPredicateComposite<A1, A2, A3> implements
ITernaryPredicate<A1, A2, A3> {

	protected final ITernaryPredicate<? super A1, ? super A2, ? super A3>[] components;

	/**
	 * Constructs a new {@code UnaryPredicateComposite} instance.
	 */
	protected TernaryPredicateComposite(
			ITernaryPredicate<? super A1, ? super A2, ? super A3>... components) {
		super();
		this.components = components;
	}

}
