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

import org.jmetis.kernel.closure.ITernaryPredicate;

/**
 * @author era
 *
 */
public class TernaryNotPredicate<A1, A2, A3> extends
TernaryPredicateDecorator<A1, A2, A3> {

	/**
	 * @param component
	 */
	public TernaryNotPredicate(ITernaryPredicate<A1, A2, A3> component) {
		super(component);
	}

	@Override
	public boolean evaluate(A1 firstArgument, A2 secondArgument,
			A3 thirdArgument) {
		return !super.evaluate(firstArgument, secondArgument, thirdArgument);
	}

}
