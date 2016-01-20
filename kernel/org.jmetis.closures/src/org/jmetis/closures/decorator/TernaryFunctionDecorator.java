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

import org.jmetis.kernel.closure.ITernaryFunction;

/**
 * @author era
 *
 */
public class TernaryFunctionDecorator<R, A1, A2, A3> implements
ITernaryFunction<R, A1, A2, A3> {

	protected final ITernaryFunction<R, A1, A2, A3> component;

	/**
	 * 
	 */
	protected TernaryFunctionDecorator(ITernaryFunction<R, A1, A2, A3> component) {
		super();
		this.component = component;
	}

	/* (non-Javadoc)
	 * @see org.jmetis.closures.ITernaryFunction#evaluate(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public R evaluate(A1 firstArgument, A2 secondArgument, A3 thirdArgument) {
		return this.component.evaluate(firstArgument, secondArgument, thirdArgument);
	}

}
