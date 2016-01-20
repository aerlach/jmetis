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

import org.jmetis.kernel.closure.IBinaryPredicate;

/**
 * @author era
 * 
 */
public abstract class BinaryPredicateComposite<A1, A2> implements
IBinaryPredicate<A1, A2> {

	protected final IBinaryPredicate<? super A1, ? super A2>[] components;

	/**
	 * Constructs a new {@code UnaryPredicateComposite} instance.
	 */
	protected BinaryPredicateComposite(
			IBinaryPredicate<? super A1, ? super A2>... components) {
		super();
		this.components = components;
	}

}
