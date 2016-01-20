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
package org.jmetis.closures.decorator;

import org.jmetis.kernel.closure.IBinaryPredicate;

/**
 * @author era
 * 
 */
public class BinaryNotPredicate<A1, A2> extends
BinaryPredicateDecorator<A1, A2> {

	/**
	 * Constructs a new {@code UnaryPredicateComposite} instance.
	 */
	public BinaryNotPredicate(IBinaryPredicate<A1, A2> component) {
		super(component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.closures.IBinaryPredicate#evaluate(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public boolean evaluate(A1 firstArgument, A2 secondArgument) {
		return !super.evaluate(firstArgument, secondArgument);
	}

}
