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
package org.jmetis.kernel.closure;

/**
 * {@code IUnaryFunction} is a function that accepts one one argument.
 * 
 * <p>
 * Example
 * </p>
 * 
 * <pre>
 *   IUnaryFunction&lt;String, Integer&gt; function = new IUnaryFunction&lt;String, Integer&gt;(){
 *     public String execute(Integer value){
 *       return Integer.toBinaryString(value);
 *     }
 *   }
 * </pre>
 * 
 * @param <A>
 *            the allowable type for the functor's argument
 * @param <R>
 *            the allowable type for the functor's return value
 * 
 * @author era
 */
public interface IUnaryFunction<R, A> {

	/**
	 * Evaluates the receiver against the given {@code argument}.
	 * 
	 * @param argument
	 *            the argument to the function
	 * @return the result of the evaluation
	 */
	R evaluate(A argument);

}
