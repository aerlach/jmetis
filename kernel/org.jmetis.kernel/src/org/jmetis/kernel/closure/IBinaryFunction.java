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
 * {@code IBinaryFunction} is a function that accepts one two argument.
 * 
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 *   IBinaryFunction&lt;Integer, Integer&gt; function = new IBinaryFunction&lt;Integer, Integer&gt;(){
 *     public boolean evaluate(Integer firstArgument, Integer secondArgument){
 *       return firstArgument % secondArgument == 0
 *     }
 *   };
 * </pre>
 * 
 * @param <R>
 *            the allowable types for the functor's return value
 * @param <A1>
 *            the allowable types for the functor's first argument.
 * @param <A2>
 *            the allowable types for the functor's second argument.
 * 
 * @author era
 */
public interface IBinaryFunction<R, A1, A2> {

	/**
	 * Evaluates the receiver against the given {@code argument}.
	 * 
	 * @param argument
	 *            the argument to the predicate
	 * @return the result of the evaluation
	 */
	R evaluate(A1 firstArgument, A2 secondArgument);

}
