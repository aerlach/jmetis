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
public class TernaryNonePredicate<A1, A2, A3> extends
TernaryPredicateComposite<A1, A2, A3> {

	/**
	 * @param components
	 */
	public TernaryNonePredicate(
			ITernaryPredicate<? super A1, ? super A2, ? super A3>... components) {
		super(components);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.closures.ITernaryPredicate#evaluate(java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
	public boolean evaluate(A1 firstArgument, A2 secondArgument,
			A3 thirdArgument) {
		for (ITernaryPredicate<? super A1, ? super A2, ? super A3> p : this.components) {
			if (p.evaluate(firstArgument, secondArgument, thirdArgument)) {
				return false;
			}
		}
		return true;
	}

}
