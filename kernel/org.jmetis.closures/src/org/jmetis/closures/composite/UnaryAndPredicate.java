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
public class UnaryAndPredicate<A> implements IUnaryPredicate<A> {

	private final IUnaryPredicate<? super A> firstPredicate;

	private final IUnaryPredicate<? super A> secondPredicate;

	/**
	 * 
	 */
	public UnaryAndPredicate(IUnaryPredicate<? super A> firstPredicate,
			IUnaryPredicate<? super A> secondPredicate) {
		super();
		this.firstPredicate = firstPredicate;
		this.secondPredicate = secondPredicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.closures.IUnaryPredicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(A argument) {
		return this.firstPredicate.evaluate(argument)
		&& this.secondPredicate.evaluate(argument);
	}

}
