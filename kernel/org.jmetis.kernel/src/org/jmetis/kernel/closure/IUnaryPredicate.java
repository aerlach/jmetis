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
 * {@code IUnaryPredicate} is a boolean function that accepts one argument
 * parameter returning a {@code boolean} value as result.
 * <p>
 * Example: filtering a list
 * </p>
 * 
 * <pre>
 *   List&lt;Integer&gt; source = Arrays.asList(1, 2, 3, 4, 5);
 *   IUnaryPredicate&lt;Integer&gt; evenNumberedPredicate = new IUnaryPredicate&lt;Integer&gt;(){
 *     public boolean evaluate(Integer value){
 *       return value % 2 == 0
 *     }
 *   };
 *   List&lt;Integer&gt; result = new ArrayList&lt;Integer&gt;();
 *   for (Integer item : source) {
 *     if (evenNumberedPredicate.evaluate(item)) {
 *       result.add(item);
 *     }
 *   }
 *   System.out.println(result);  // =&gt; [2, 4]
 * </pre>
 * 
 * @param <A>
 *            the allowable types for the argument.
 * 
 * @author era
 */
public interface IUnaryPredicate<A> {

	/**
	 * Evaluates the boolean function against the given {@code argument}.
	 * 
	 * @param argument
	 *            the argument to the boolean function
	 * @return the result of the evaluation
	 */
	boolean evaluate(A argument);

}
