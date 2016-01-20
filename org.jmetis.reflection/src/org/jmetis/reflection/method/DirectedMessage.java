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
package org.jmetis.reflection.method;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jmetis.reflection.Primitives;

/**
 * {@code DirectedMessage} allows to specify a static or non-static method to
 * invoke in a declarative fashion.
 * 
 * <p>
 * Usage: Specify "receiver"/"methodName", optionally specify arguments.
 * Afterwards, you may invoke the method any number of times, obtaining the
 * invocation result.
 * 
 * @author aerlach
 */
public class DirectedMessage {

	private Object receiver;

	private String methodName;

	private Object[] arguments;

	private Method method;

	public DirectedMessage(Object receiver, String methodName,
			Object... arguments) {
		super();
		this.receiver = Primitives.mustNotBeNull("receiver", receiver);
		this.methodName = Primitives.mustNotBeNullOrEmpty("methodName",
				methodName);
		this.arguments = Primitives.mustNotBeNull("arguments", arguments);
	}

	/**
	 * @return the target object on which to call the target method.
	 */
	public Object getReceiver() {
		return receiver;
	}

	protected Class<?> getReceiverClass() {
		return receiver.getClass();
	}

	/**
	 * @return the name of the method to be invoked.
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @return the arguments for the method invocation.
	 */
	public Object[] getArguments() {
		return arguments.clone();
	}

	/**
	 * Find a matching method with the specified name for the specified
	 * arguments.
	 * 
	 * @return a matching method, or {@code null} if none
	 */
	protected Method findMatchingMethod() {
		String methodName = getMethodName();
		Object[] arguments = getArguments();
		int argumentCount = arguments.length;
		Method[] candidates = getReceiverClass().getMethods();
		int minTypeDiffWeight = Integer.MAX_VALUE;
		Method matchingMethod = null;
		for (Method candidate : candidates) {
			if (candidate.getName().equals(methodName)) {
				Class<?>[] paramTypes = candidate.getParameterTypes();
				if (paramTypes.length == argumentCount) {
					int typeDiffWeight = getTypeDifferenceWeight(paramTypes,
							arguments);
					if (typeDiffWeight < minTypeDiffWeight) {
						minTypeDiffWeight = typeDiffWeight;
						matchingMethod = candidate;
					}
				}
			}
		}
		return matchingMethod;
	}

	/**
	 * Return the prepared Method object that will be invoked.
	 * 
	 * @return the prepared Method object (never {@code null})
	 * @see #invoke
	 */
	protected Method getPreparedMethod() throws NoSuchMethodException {
		if (method == null) {
			Class<?> receiverClass = getReceiverClass();
			String methodName = getMethodName();
			Object[] arguments = getArguments();
			Class<?>[] argumentTypes = new Class[arguments.length];
			for (int i = 0, n = arguments.length; i < n; ++i) {
				if (arguments[i] != null) {
					argumentTypes[i] = arguments[i].getClass();
				} else {
					argumentTypes[i] = Object.class;
				}
			}
			try {
				method = receiverClass.getMethod(methodName, argumentTypes);
			} catch (NoSuchMethodException ex) {
				// Just rethrow exception if we can't get any match.
				method = findMatchingMethod();
				if (method == null) {
					throw ex;
				}
			}
		}
		return method;
	}

	/**
	 * Invoke the specified method.
	 * 
	 * @return the object (possibly {@code null}) returned by the method
	 *         invocation, or {@code null} if the method has a void return type
	 * @throws InvocationTargetException
	 *             if the target method threw an exception
	 * @throws IllegalAccessException
	 *             if the target method couldn't be accessed
	 * @throws NoSuchMethodException
	 * @see #prepare
	 */
	public Object invoke() throws InvocationTargetException,
			IllegalAccessException, NoSuchMethodException {
		Object receiver = getReceiver();
		Method preparedMethod = getPreparedMethod();
		return preparedMethod.invoke(receiver, getArguments());
	}

	protected boolean isAssignableValue(Class<?> type, Object value) {
		return value != null ? Primitives.isAssignableFrom(type, value
				.getClass()) : !type.isPrimitive();
	}

	/**
	 * Algorithm that judges the match between the declared parameter types of a
	 * candidate method and a specific list of arguments that this method is
	 * supposed to be invoked with.
	 * <p>
	 * Determines a weight that represents the class hierarchy difference
	 * between types and arguments. A direct match, i.e. type Integer -> arg of
	 * class Integer, does not increase the result - all direct matches means
	 * weight 0. A match between type Object and arg of class Integer would
	 * increase the weight by 2, due to the superclass 2 steps up in the
	 * hierarchy (i.e. Object) being the last one that still matches the
	 * required type Object. Type Number and class Integer would increase the
	 * weight by 1 accordingly, due to the superclass 1 step up the hierarchy
	 * (i.e. Number) still matching the required type Number. Therefore, with an
	 * arg of type Integer, a constructor (Integer) would be preferred to a
	 * constructor (Number) which would in turn be preferred to a constructor
	 * (Object). All argument weights get accumulated.
	 * 
	 * @param paramTypes
	 *            the parameter types to match
	 * @param args
	 *            the arguments to match
	 * @return the accumulated weight for all arguments
	 */
	protected int getTypeDifferenceWeight(Class<?>[] paramTypes, Object[] args) {
		int result = 0;
		for (int i = 0; i < paramTypes.length; i++) {
			if (!isAssignableValue(paramTypes[i], args[i])) {
				return Integer.MAX_VALUE;
			}
			if (args[i] != null) {
				Class<?> paramType = paramTypes[i];
				Class<?> superClass = args[i].getClass().getSuperclass();
				while (superClass != null) {
					if (paramType.equals(superClass)) {
						result = result + 2;
						superClass = null;
					} else if (Primitives.isAssignableFrom(paramType,
							superClass)) {
						result = result + 2;
						superClass = superClass.getSuperclass();
					} else {
						superClass = null;
					}
				}
				if (paramType.isInterface()) {
					result = result + 1;
				}
			}
		}
		return result;
	}

}
