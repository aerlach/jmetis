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
package org.jmetis.collections.iterable;

import java.util.Collection;

import org.jmetis.kernel.closure.IUnaryFunction;
import org.jmetis.kernel.closure.IUnaryPredicate;

/**
 * {@code ICollection} extends {@link Collection} by providing closures in
 * addition to {@link Iterators}. By use closures we mean replacing a loop like:
 * 
 * <pre>
 * Iterator it = collection.iterator();
 * while (it.hasNext()) {
 * 	Object each = it.next();
 * 	//...
 * }
 * </pre>
 * 
 * with:
 * 
 * <pre>
 * collection.apply(new OneArgumentVoidStrategy() {
 * 	public void execute(Object argument) {
 * 		// ...
 * 	}
 * });
 * </pre>
 * 
 * One reason is that the iteration body is now an object. This object may be an
 * anonymous inner specialization of the interface Strategy or the instance of
 * any ordinary class that implements the strategy interface (see blocks in
 * Java). By turning the body of the iteration into a strategy object, it opens
 * possibilities for reuse. Another reason is that the apply function is a lot
 * easier to write than an iteration, especially if the collection is something
 * not inherently flat, like a binary tree or an M-way tree. The natural way to
 * visit every node of a binary tree is with a recursive routine like:
 * 
 * <pre>
 * public void apply(OneArgumentVoidStrategy strategy) {
 * 	leftBranch.forEach(closure);
 * 	strategy.execute(this);
 * 	rightBranch.forEach(closure);
 * }
 * </pre>
 * 
 * This sort of traversal is much harder to write when using external iterators
 * such as Iterator or Enumerator. The collections also support variations, for
 * example ways to filter or otherwise process the contents.
 * 
 * @author era
 */
public interface ICollection<E> extends Collection<E> {

	/**
	 * Evaluate if the execution of the given {@code predicate} returns {@code
	 * true} for EACH value stored in the Collection. Example:
	 * 
	 * <pre>
	 * //Check if elements are even numbers:
	 * ICollection&lt;Integer&gt; collection = new Collection(Arrays.asList(10, 20));
	 * IPredicate&lt;Integer&gt; evenNumberPredicate = new IPredicate&lt;Integer&gt;() {
	 * 	public boolean evaluate(Integer value) {
	 * 		return value % 2 == 0;
	 * 	}
	 * };
	 * if (collection.allSatisfy(evenNumberPredicate)) {
	 * 	System.out.println(&quot;All elements are even numbers&quot;);
	 * }
	 * </pre>
	 * 
	 * @param predicate
	 *            the predicate to use
	 * @return {@code true} if the execution of the strategy could be done for
	 *         each value {@code false} otherwise.
	 */

	boolean allSatisfy(IUnaryPredicate<E> predicate);

	/**
	 * Evaluate if the execution of the given {@code predicate} returns {@code
	 * true} for ANY value stored in the Collection. Example:
	 * 
	 * <pre>
	 * ICollection&lt;Integer&gt; collection = new Collection(Arrays.asList(10, 15, 20));
	 * IPredicate&lt;Integer&gt; evenNumberPredicate = new IPredicate&lt;Integer&gt;() {
	 * 	public boolean evaluate(Integer value) {
	 * 		return value % 2 == 0;
	 * 	}
	 * };
	 * if (collection.anySatisfy(evenNumberPredicate)) {
	 * 	System.out.println(&quot;There is an even number&quot;);
	 * }
	 * </pre>
	 * 
	 * @param predicate
	 *            the predicate to use
	 * @return {@code true} if the execution of the strategy could be done for
	 *         each value {@code false} otherwise.
	 */
	boolean anySatisfy(IUnaryPredicate<E> predicate);

	/**
	 * Executes the <code>strategy</code> on each element of the receiver until
	 * the strategy's execution returns <code>false</code> or all elements have
	 * been processed.
	 * 
	 * @param <code>strategy</code> the strategy to use
	 */
	void apply(IUnaryPredicate<E> predicate);

	/**
	 * Executes the <code>strategy</code> on each element of the receiver until
	 * the strategy's execution returns <code>Null</code> as result or all
	 * elements have been processed.
	 * 
	 * @param <code>strategy</code> the strategy to use
	 */
	void apply(OneArgumentObjectStrategy strategy);

	/**
	 * Executes the <code>strategy</code> on each element of the receiver.
	 * 
	 * @param <code>strategy</code> the strategy to use
	 */
	void apply(OneArgumentVoidStrategy strategy);

	/**
	 * Executes the <code>strategy</code> on each element of the receiver and
	 * collect the result of the strategy's evaluation in a collection. Example:
	 * 
	 * <pre>
	 *   ICollection numCollection;
	 * 
	 *  //Simple strategy:
	 *   OneArgumentObjectStrategy multiplyStrategy = new  OneArgumentObjectStrategy(){
	 *     public void execute(Object arg1){
	 *       Integer res = ((Integer)arg1);
	 *       return new Integer(res.intValue() * res.intValue()))
	 *     }
	 *   }
	 * 
	 *   numCollection.add(new Integer(10));
	 *   numCollection.add(new Integer(20));
	 *   ArrayList result = new ArrayList();
	 *   result.addAll(numCollection.collect(multiplyStrategy));
	 * 
	 *   for(Iterator it = result.iterator(); it.hasNext();){
	 *     System.out.println(it.getNext());
	 *   }
	 * </pre>
	 * 
	 * @param <code>strategy</code> the strategy to use
	 * @return collection of the result of the strategy's evaluation on the
	 *         receiver's elements
	 */
	Collection<E> collect(IUnaryFunction<E, E> function);

	/**
	 * First executes the <code>collectStrategy</code> on each element of the
	 * receiver and then execute the
	 * <code>selectStrategy<code> on the result array of the <code>collectStrategy</code>
	 * 's evaluation. result of the strategy's evaluation in a collection.
	 * Example:
	 * 
	 * <pre>
	 *   ICollection numCollection;
	 * 
	 *   //Example for a collectionStrategy:
	 *   OneArgumentObjectStrategy multiplyStrategy = new OneArgumentObjectStrategy(){
	 *     public void execute(Object arg1){
	 *       Integer res = ((Integer)arg1);
	 *       return new Integer(res.intValue() * res.intValue()))
	 *     }
	 *   }
	 *   //Example for a selectionStrategy:
	 *   IPredicate checkGreaterThan100Strategy = new IPredicate(){
	 *     public boolean execute(Object arg1){
	 *       Integer value = (Integer)arg1;
	 *       return (value.intValue &gt; 100);
	 *     }
	 *   }
	 * 
	 *   numCollection.add(new Integer(10));
	 *   numCollection.add(new Integer(20));
	 *   ArrayList result = new ArrayList();
	 *   result.addAll(numCollection.collectThenSelect(multiplyStrategy, checkGreaterThan100Strategy));
	 *   System.out.println(&quot;After performing the CollectionStrategy there are &quot;+ new Integer(result.size())+&quot; element(s) greater than 100.&quot;);
	 *   for(Iterator it = result.iterator(); it.hasNext();){
	 *     System.out.println(it.getNext());
	 *   }
	 * </pre>
	 * 
	 * @param <code>collectStrategy</code> the strategy to use for the
	 *        collection
	 * @param <code>selectStrategy</code> the strategy to use for the selection
	 * @return collection of the result of the strategy's evaluation on the
	 *         receiver's elements
	 */
	Collection<E> collectThenSelect(OneArgumentObjectStrategy collectStrategy,
			IUnaryPredicate<E> selectStrategy);

	/**
	 * Evaluate the given <code>strategy</code> with each of the receiver's
	 * elements as the argument. Answer the number of elements that answered
	 * true. Example:
	 * 
	 * <pre>
	 *   ICollection numCollection;
	 *   IPredicate checkGreaterThan100Strategy = new IPredicate(){
	 *     public boolean execute(Object arg1){
	 *       Integer value = (Integer)arg1;
	 *       return (value.intValue() &gt; 100);
	 *     }
	 *   }
	 * 
	 *   numCollection.add(new Integer(1000));
	 *   numCollection.add(new Integer(20));
	 *   numCollection.add(new Integer(200));
	 *   int result = count(checkGreaterThan100Strategy);
	 *   System.out.println(&quot;There are &quot;+ new Integer(result)+&quot; element(s) greater than 100.&quot;);
	 * </pre>
	 * 
	 * @param <code>strategy</code> the strategy to use
	 * @return the number of elements that answered true.
	 */
	int count(IUnaryPredicate<E> predicate);

	/**
	 * Get the first object of the receiver which answers the evaluation of the
	 * <code>strategy</code> with <code>true</code>. If none of the objects
	 * answers with <code>true</code> the return value is NULL. Example:
	 * 
	 * <pre>
	 *   ICollection numCollection;
	 *   IPredicate checkGreaterThan100Strategy = new IPredicate(){
	 *     public boolean execute(Object arg1){
	 *       Integer value = (Integer)arg1;
	 *       return (value.intValue() &gt; 100);
	 *     }
	 *   }
	 * 
	 *   numCollection.add(new Integer(1000));
	 *   numCollection.add(new Integer(20));
	 *   numCollection.add(new Integer(200));
	 *   Object result = detect(checkGreaterThan100Strategy);
	 *   if (result == null){ System.out.println(&quot;There is no value greater than 100&quot;);
	 *   } else {
	 *     System.out.println(&quot;First number greater then 100 :&quot;+ (Integer)result);
	 *   }
	 * </pre>
	 * 
	 * @param <code>strategy</code> the strategy to use
	 * @return first value of the receiver which answers the evaluation of the
	 *         <code>strategy</code> with <code>true</code> or NULL if none of
	 *         the objects answers with <code>true</code>.
	 */
	E detect(IUnaryPredicate<E> strategy);

	/**
	 * Get the first object of the receiver which answers the evaluation of the
	 * <code>strategy</code> with <code>true</code>. If none of the objects
	 * answeres with <code>true</code> the return value is
	 * <code>defaultValue</code>. Example:
	 * 
	 * <pre>
	 *   ICollection numCollection;
	 *   IPredicate checkGreaterThan100Strategy = new IPredicate(){
	 *     public boolean execute(Object arg1){
	 *       Integer value = (Integer)arg1;
	 *       return (value.intValue() &gt; 100);
	 *     }
	 *   }
	 * 
	 *   Integer defaultValue = new Integer(815);
	 *   numCollection.add(new Integer(1000));
	 *   numCollection.add(new Integer(20));
	 *   numCollection.add(new Integer(200));
	 *   Object result = detect(checkGreaterThan100Strategy, defaultValue);
	 *   if (result == defaultValue){ System.out.println(&quot;There is no value greater than 100&quot;);
	 *   } else {
	 *     System.out.println(&quot;First number greater then 100 :&quot;+ (Integer)result);
	 *   }
	 * </pre>
	 * 
	 * @param <code>strategy</code> the strategy to use
	 * @param <code>defaultValue</code> the default value to use
	 * @return first value of the receiver which answers the evaluation of the
	 *         <code>strategy</code> with <code>true</code> or
	 *         <code>defaultValue</code> if none of the objects answers with
	 *         <code>true</code>.
	 */
	E detect(IUnaryPredicate<E> strategy, E defaultValue);

	/**
	 * Split the receivers contents into collections of elements for which
	 * <code>groupStrategy</code> returns the same results. Example:
	 * 
	 * <pre>
	 * //Group Strategy:
	 *   OneArgumentObjectStrategy groupStrategy = new OneArgumentObjectStrategy(){
	 *     public Object execute(Object arg1){
	 *             Integer value = (Integer)arg1;
	 *       if (value.intValue() &gt; 100){
	 *         return new Integer(1);
	 *       } else {
	 *         return new Integer(0);
	 *       }
	 *     }
	 *   }
	 * 
	 *      ICollection numCollection;
	 *      numCollection.addIfAbsent(new Integer(111));
	 *      numCollection.addIfAbsent(new Integer(22));
	 *      numCollection.addIfAbsent(new Integer(332));
	 *      numCollection.addIfAbsent(new Integer(44));
	 *      Map result = (Map)numCollection.groupBy(groupStrategy);
	 *      Set listOfKeys = result.keySet();
	 *      Iterator keyIterator = listOfKeys.iterator();
	 *      while(keyIterator.hasNext()){
	 *              Object key = keyIterator.next();
	 *        ArrayedList group = (ArrayedList)result.get(key);
	 *              if( ((Integer)key).intValue() == 0){
	 *           System.out.println(&quot;Elements smaller than 100&quot;:);
	 *        } else {
	 *           System.out.println(&quot;Elements bigger than 100&quot;:);
	 *        }
	 *               for(int itemIndex = 0; itemIndex &lt;= group.size();itemIndex++){
	 *          System.out.println(group.get(itemIndex).toString());
	 *        }
	 *      }
	 * </pre>
	 * 
	 * @param <code>groupStrategy</code> the strategy to separate the elements
	 *        into groups
	 * @return a collection of the different groups containing a set of elements
	 *         for each group.
	 */
	Collection<E> groupBy(OneArgumentObjectStrategy groupStrategy);

	/**
	 * Split the receivers contents into collections of elements for which
	 * <code>groupStrategy</code> returns the same results, and return those
	 * collections allowed by <code>havingStrategy</code>. Example:
	 * 
	 * <pre>
	 * //Group Strategy:
	 *   OneArgumentIntegerStrategy groupStrategy = new OneArgumentIntegerStrategy(){
	 *     public int execute(Object arg1){
	 *       Integer value = (Integer)arg1;
	 *       if (value.intValue() &gt; 100){
	 *         return 1;
	 *       } else {
	 *         return 0;
	 *       }
	 *     }
	 *   }
	 * //Having Strategy:
	 *   IPredicate evenNumStrategy = new IPredicate(){
	 *     public boolean execute(Object arg){
	 *       if (arg instanceOf(Integer)){
	 *         Integer val = (Integer)arg);
	 *         if( val.intValue() % 2 == 0){
	 *           return true;
	 *         } else {
	 *           return false;
	 *         }
	 *       }
	 *     }
	 *     return false;
	 *   }
	 *  ICollection numCollection;
	 *  numCollection.addIfAbsent(new Integer(111));
	 *  numCollection.addIfAbsent(new Integer(22));
	 *  numCollection.addIfAbsent(new Integer(332));
	 *  numCollection.addIfAbsent(new Integer(44));
	 *  Map result = (Map)numCollection.groupBy(groupStrategy,evenNumStrategy);
	 *  Set listOfKeys = result.keySet();
	 *  Iterator keyIterator = listOfKeys.iterator();
	 *  while(keyIterator.hasNext()){
	 *    Object key = keyIterator.next();
	 *    ArrayedList group = (ArrayedList)result.get(key);
	 *    if( ((Integer)key).intValue() == 0){
	 *      System.out.println(&quot;Even numbers smaller than 100&quot;:);
	 *    } else {
	 *      System.out.println(&quot;Even numbers bigger than 100&quot;:);
	 *    }
	 *    for(int itemIndex = 0; itemIndex &lt;= group.size();itemIndex++){
	 *      System.out.println(group.get(itemIndex).toString());
	 *    }
	 *  }
	 * </pre>
	 * 
	 * @param <code>groupStrategy</code> the strategy to separate the elements
	 *        into groups
	 * @param <code>havingStrategy</code> the strategy to do the selection
	 * @return a collection of the different groups containing a set of elements
	 *         for each group.
	 */
	Collection<E> groupByHaving(OneArgumentIntegerStrategy groupStrategy,
			IUnaryPredicate<E> havingStrategy);

	/**
	 * Split the receivers contents into collections of elements for which
	 * <code>groupStrategy</code> returns the same results, and return those
	 * collections allowed by <code>havingStrategy</code>. Example:
	 * 
	 * <pre>
	 * //Group Strategy:
	 *   OneArgumentObjectStrategy groupStrategy = new OneArgumentObjectStrategy(){
	 *     public Object execute(Object arg1){
	 *       Integer value = (Integer)arg1;
	 *       if (value.intValue() &gt; 100){
	 *         return new Integer(1);
	 *       } else {
	 *         return new Integer(0);
	 *       }
	 *     }
	 *   }
	 * //Having Strategy:
	 *     IPredicate evenNumStrategy = new IPredicate(){
	 *       public boolean execute(Object arg){
	 *         if (arg instanceOf(Integer)){
	 *           Integer val = (Integer)arg);
	 *           if( val.intValue() % 2 == 0){
	 *             return true;
	 *           } else {
	 *             return false;
	 *           }
	 *         }
	 *       }
	 *       return false;
	 *     }
	 * 
	 *  ICollection numCollection;
	 *  numCollection.addIfAbsent(new Integer(111));
	 *  numCollection.addIfAbsent(new Integer(22));
	 *  numCollection.addIfAbsent(new Integer(332));
	 *  numCollection.addIfAbsent(new Integer(44));
	 *  Map result = (Map)numCollection.groupBy(groupStrategy,evenNumStrategy);
	 *  Set listOfKeys = result.keySet();
	 *  Iterator keyIterator = listOfKeys.iterator();
	 *  while(keyIterator.hasNext()){
	 *    Object key = keyIterator.next();
	 *    ArrayedList group = (ArrayedList)result.get(key);
	 *    if( ((Integer)key).intValue() == 0){
	 *      System.out.println(&quot;Even numbers smaller than 100&quot;:);
	 *    } else {
	 *      System.out.println(&quot;Even numbers bigger than 100&quot;:);
	 *    }
	 *    for(int itemIndex = 0; itemIndex &lt;= group.size();itemIndex++){
	 *      System.out.println(group.get(itemIndex).toString());
	 *    }
	 *  }
	 * </pre>
	 * 
	 * @param <code>groupStrategy</code> the strategy to separate the elements
	 *        into groups
	 * @param <code>havingStrategy</code> the strategy to do the selection
	 * @return a collection of the different groups containing a set of elements
	 *         for each group.
	 */
	Collection<E> groupByHaving(OneArgumentObjectStrategy groupStrategy,
			IUnaryPredicate<E> havingStrategy);

	/**
	 * Evaluate the result of iteratively evaluating the specified
	 * <code>strategy</code> using the previous result of evaluating
	 * <code>strategy</code> and each element of the receiver as arguments. The
	 * first argument of the <code>strategy</code> represents the result of the
	 * previous iteration and the second argument represents an element in the
	 * receiver. The initial value for the first <code>strategy</code> argument
	 * is the argument <code>initialValue</code>. Example:
	 * 
	 * <pre>
	 *  ICollection sumCollection;
	 *  sumCollection.addIfAbsent(new Integer(1));
	 *  sumCollection.addIfAbsent(new Integer(2));
	 *  sumCollection.addIfAbsent(new Integer(3));
	 *  sumCollection.addIfAbsent(new Integer(4));
	 * //strategy to sum two Integer values:
	 *  TwoArgumentObjectStrategy sumNumStrategy = new TwoArgumentObjectStrategy(){
	 *    public Object execute(Object arg1, Object arg2){
	 *      int sum = ((Integer)arg1).intValue() + ((Integer)arg2).intValue();
	 *      return new Integer(sum);
	 *    }
	 *  }
	 * 
	 *  Integer start = new Integer(0);
	 *  Object result = sumCollection.injectInto(start,sumNumStrategy);
	 *  System.out.println(&quot;Sum of collection and &quot;+ start.toString()+ &quot; = &quot; + ((Integer)result).toString();
	 *  //result = 0+1+2+3+4 = 10                 &lt;/code&gt;
	 * </pre>
	 * 
	 * @param <code>initialValue</code> the value to start with
	 * @param <code>strategy</code> strategy to evaluate
	 * @return the result of iteratively evaluating the specified
	 *         <code>strategy</code> using the previous result of evaluating
	 *         <code>strategy</code> and each element of the receiver as
	 *         arguments
	 */
	Object injectInto(Object initialValue, TwoArgumentObjectStrategy strategy);

	/**
	 * Evaluate if the execution of the <code>strategy</code> returns
	 * <code>true</code> for NONE of the values stored in the Collection.
	 * Example:
	 * 
	 * <pre>
	 *  //Check if elements stored are even numbers:
	 *   IPredicate evenNumStrategy = new IPredicate(){
	 *     public boolean execute(Object arg){
	 *       if (arg instanceOf(Integer)){
	 *         Integer val = (Integer)arg);
	 *         if( val.intValue() % 2 == 0){
	 *           return true;
	 *         } else {
	 *           return false;
	 *         }
	 *       }
	 *     }
	 *     return false;
	 *   }
	 *  ICollection newAbstrColl;
	 *  newAbstrColl.addIfAbsent(new Integer(11));
	 *  newAbstrColl.addIfAbsent(new Integer(21));
	 *  newAbstrColl.addIfAbsent(new Integer(33));
	 *  newAbstrColl.addIfAbsent(new Integer(47));
	 *  boolean hasNoEven = newAbstrColl.noneSatisfy(evenNumStrategy);
	 *  if(hasNoEven) System.out.println(&quot;There is no even number&quot;);
	 * </pre>
	 * 
	 * @param <code>strategy</code> the strategy to execute
	 * @return <code>true</code> if the execution of the strategy could be done
	 *         for each value <code>false</code> otherwise.
	 * @see ICollection#noneSatisfy(IUnaryPredicate)
	 */
	boolean noneSatisfy(IUnaryPredicate<E> strategy);

	/**
	 * Evaluate a collection of the receiver's elements which do not fulfill the
	 * evaluation of the specified <code>strategy</code>. The elements found are
	 * returned in a collection. Example:
	 * 
	 * <pre>
	 * //Check if elements stored are even numbers:
	 *  IPredicate evenNumStrategy = new IPredicate(){
	 *    public boolean execute(Object arg){
	 *      if (arg instanceOf(Integer)){
	 *        Integer val = (Integer)arg);
	 *        if( val.intValue() % 2 == 0){
	 *          return true;
	 *        } else {
	 *          return false;
	 *        }
	 *      }
	 *    }
	 *    return false;
	 *  }
	 *  ICollection newCollection;
	 *  newCollection.addIfAbsent(new Integer(11));
	 *  newCollection.addIfAbsent(new Integer(22));
	 *  newCollection.addIfAbsent(new Integer(33));
	 *  newCollection.addIfAbsent(new Integer(44));
	 *  Collection oddNumbers = newCollection.reject(evenNumStrategy);
	 *  if(oddNumbers.size() != 0) System.out.println(&quot;There are &quot;+ new Integer(oddNumbers.size())+&quot; odd numbers&quot;);
	 * </pre>
	 * 
	 * @param <code>strategy</code> the strategy to evaluate
	 * @return a collection of elements which do not fulfill the evaluation of
	 *         <code>strategy</code>
	 */
	Collection<E> reject(IUnaryPredicate<E> strategy);

	/**
	 * Select the receiver's elments which fulfil the <code>strategy</code>'s
	 * evaluation and return them in a collection. Example:
	 * 
	 * <pre>
	 *  //Check if elements stored are even numbers:
	 *    IPredicate evenNumStrategy = new IPredicate(){
	 *      public boolean execute(Object arg){
	 *        if (arg instanceOf(Integer)){
	 *          Integer val = (Integer)arg);
	 *          if( val.intValue() % 2 == 0){
	 *            return true;
	 *          } else {
	 *            return false;
	 *          }
	 *        }
	 *      }
	 *      return false;
	 *    }
	 *  ICollection numCollection;
	 *  numCollection.addIfAbsent(new Integer(1));
	 *  numCollection.addIfAbsent(new Integer(20));
	 *  numCollection.addIfAbsent(new Integer(33));
	 *  numCollection.addIfAbsent(new Integer(40));
	 *  Collection evenNumbers = numCollection.reject(evenNumStrategy);
	 *  if(evenNumbers.size() != 0) System.out.println(&quot;There are &quot;+ new Integer(evenNumbers.size())+&quot; even numbers&quot;);
	 * </pre>
	 * 
	 * @param <code>strategy</code> the strategy to use.
	 * @return collection of elements which fulfill the <code>strategy</code>'s
	 *         evaluation
	 */
	Collection<E> select(IUnaryPredicate<E> strategy);

	/**
	 * Select the receiver's elements which fulfill the <code>strategy</code>'s
	 * evaluation and return them in a collection. Example:
	 * 
	 * <pre>
	 *  ICollection numCollection;
	 *  //Example for a collectionStrategy:
	 *  OneArgumentObjectStrategy multiplyStrategy = new OneArgumentObjectStrategy(){
	 *    public void execute(Object arg1){
	 *      Integer res = ((Integer)arg1);
	 *      return new Integer(res.intValue() * res.intValue()))
	 *    }
	 *  }
	 *  //Example for a selectionStrategy:
	 *   IPredicate checkGreaterThan100Strategy = new IPredicate(){
	 *     public boolean execute(Object arg1){
	 *       Integer value = (Integer)arg1;
	 *       return (value.intValue() &gt; 100);
	 *     }
	 *   }
	 * 
	 *   numCollection.add(new Integer(100));
	 *         numCollection.add(new Integer(200));
	 *   ArrayList result = new ArrayList();
	 *   result.addAll(numCollection.collectThenSelect(checkGreaterThan100Strategy,multiplyStrategy));
	 *   for(Iterator it = result.iterator(); it.hasNext();){
	 *     System.out.println(it.getNext());
	 *     //output = 200*200 = 40000
	 *   }
	 * </pre>
	 * 
	 * @param <code>selectStrategy</code> the strategy to use for selection
	 *        purpose
	 * @param <code>collectStrategy</code> the strategy to us for collection
	 *        purpose
	 * @return collection of the result of the <code>selectStrategy</code>'s
	 *         evaluation on the receiver's elements, filtered using the
	 *         additional <code>collectionStrategy</code>.
	 */
	Collection<E> selectThenCollect(IUnaryPredicate<E> selectStrategy,
			OneArgumentObjectStrategy collectStrategy);

}
