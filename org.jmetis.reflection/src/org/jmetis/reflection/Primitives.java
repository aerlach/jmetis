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
package org.jmetis.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <code>Primitives</code>
 * 
 * @author aerlach
 */
public class Primitives {

	public final static Type[] EMPTY_TYPE_ARRAY = {};

	private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_LOOKUP = new HashMap<Class<?>, Class<?>>();

	/**
	 * Instantiating <code>Primitives</code> does not make sense because it
	 * contain only static methods. The constructor is protected to allow
	 * subclassing. In the case the constructor is called a
	 * {@link java.lang.UnsupportedOperationException} is thrown.
	 * 
	 * @throws java.lang.UnsupportedOperationException
	 */
	protected Primitives() {
		super();
		throw new UnsupportedOperationException();
	}

	protected static String localizedMessage(String key, Object... arguments) {
		return null;
	}

	/**
	 * Asserts that the given {@code value} is not {@code null}. If the given
	 * {@code value} is {@code null} a {@link NullPointerException} is thrown
	 * with the given {@code name}.
	 */
	public static <T> T mustNotBeNull(String name, T value) {
		if (value == null) {
			throw new NullPointerException(Primitives.localizedMessage(
					"mustNotBeNull", name)); //$NON-NLS-1$
		}
		return value;
	}

	/**
	 * Asserts that the given {@code value} is not {@code null}, and not an
	 * empty string. If the given {@code value} is {@code null} a
	 * {@link NullPointerException} is thrown with the given {@code name}, if
	 * the given {@code value} is an empty string an
	 * {@link IllegalArgumentException} is thrown with the given {@code name}.
	 */
	public static String mustNotBeNullOrEmpty(String name, String value) {
		if (Primitives.mustNotBeNull(name, value).length() == 0) {
			throw new IllegalArgumentException(Primitives.localizedMessage(
					"mustNotBeEmpty", name)); //$NON-NLS-1$
		}
		return value;
	}

	/**
	 * Check if the given {@code member} is a static {@link Member}, i.e. static
	 * field, or static method.
	 * 
	 * @param member
	 *            the member to check
	 * @return {@code true} if the given {@code member} is a static member;
	 *         otherwise {@code false}
	 */
	public static boolean isStaticMember(Member member) {
		return member != null && Modifier.isStatic(member.getModifiers());
	}

	public static boolean isPrimitiveType(Class<?> type) {
		return type != null && type.isPrimitive();
	}

	/**
	 * Check if the given {@code type} represents a primitive wrapper, i.e.
	 * Boolean, Byte, Character, Double, Float, Integer, Long, or Short.
	 * 
	 * @param type
	 *            the class to check
	 * @return {@code true} if the given class is a primitive wrapper class;
	 *         otherwise {@code false}
	 */
	public static boolean isPrimitiveWrapper(Class<?> type) {
		return Primitives.PRIMITIVE_WRAPPER_LOOKUP.containsKey(type);
	}

	/**
	 * Check if the right-hand side type may be assigned to the left-hand side
	 * type, assuming setting by reflection. Considers primitive wrapper classes
	 * as assignable to the corresponding primitive types.
	 * 
	 * @param firstType
	 *            the target type
	 * @param secondType
	 *            the value type that should be assigned to the target type
	 * @return if the target type is assignable from the value type
	 */
	public static boolean isAssignableFrom(Class<?> firstType,
			Class<?> secondType) {
		return Primitives.mustNotBeNull("firstType", firstType)
				.isAssignableFrom(
						Primitives.mustNotBeNull("secondType", secondType))
				|| firstType.equals(Primitives.PRIMITIVE_WRAPPER_LOOKUP
						.get(secondType));
	}

	/**
	 * Return all interfaces that the given class implements as array, including
	 * ones implemented by super-classes.
	 * <p>
	 * If the class itself is an interface, it gets returned as sole interface.
	 * 
	 * @param type
	 *            the class to analyze for interfaces
	 * @return all interfaces that the given {@code type} implements as array
	 */
	public static Class<?>[] allInterfacesOf(Class<?> type) {
		if (Primitives.mustNotBeNull("type", type).isInterface()) {
			return new Class[] { type };
		}
		List<Class<?>> allInterfaces = new ArrayList<Class<?>>();
		do {
			for (Class<?> currentInterface : type.getInterfaces()) {
				if (!allInterfaces.contains(currentInterface)) {
					allInterfaces.add(currentInterface);
				}
			}
			type = type.getSuperclass();
		} while (type != null);
		return allInterfaces.toArray(new Class<?>[allInterfaces.size()]);
	}

	/**
	 * Check if the given {@code type} is a {@link ParameterizedType}.
	 * 
	 * @param type
	 *            the type to check
	 * @return {@code true} if the given {@code type} is a
	 *         {@link ParameterizedType}
	 */
	public static boolean isParameterizedType(Type type) {
		return type instanceof ParameterizedType;
	}

	public static Type[] actualTypeArgumentsOf(Type type) {
		if (Primitives.isParameterizedType(type)) {
			return ((ParameterizedType) type).getActualTypeArguments();
		}
		return Primitives.EMPTY_TYPE_ARRAY;
	}

	protected static <T extends AccessibleObject> T[] accessibleObjects(
			T[] accessibleObjects) throws SecurityException {
		AccessibleObject.setAccessible(accessibleObjects, true);
		return accessibleObjects;
	}

	protected static Method accessibleDeclaredMethod(Class<?> declaringClass,
			String methodName, Class<?>[] parameterTypes)
			throws SecurityException, NoSuchMethodException {
		Method declaredMethod = declaringClass.getDeclaredMethod(methodName,
				parameterTypes);
		declaredMethod.setAccessible(true);
		return declaredMethod;
	}

	/**
	 * Returns the declared fields for the given {@code declaringClass}.
	 * Requires security policy: 'permission java.lang.RuntimePermission
	 * "accessDeclaredMembers";'
	 * 
	 * @return the declared fields for the given {@code declaringClass}
	 */
	public static Field[] declaredFieldsOf(final Class<?> declaringClass)
			throws SecurityException {
		if (System.getSecurityManager() != null) {
			return AccessController
					.doPrivileged(new PrivilegedAction<Field[]>() {
						public Field[] run() {
							return declaringClass.getDeclaredFields();
						}
					});
		}
		return declaringClass.getDeclaredFields();
	}

	protected static Field basicDeclaredFieldFor(Class<?> declaringClass,
			String fieldName) throws NoSuchFieldException {
		NoSuchFieldException exception = null;
		Class<?> currentClass = declaringClass;
		while (currentClass != null) {
			try {
				Field field = declaringClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException ex) {
				if (exception == null) {
					exception = ex;
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		if (exception != null) {
			throw exception;
		}
		return null;
	}

	/**
	 * Returns the declared field for the given {@code declaredClass}, and
	 * {@code fieldName}. Requires security policy: 'permission
	 * java.lang.RuntimePermission "accessDeclaredMembers";'
	 * 
	 * @return Field the declared field for the given {@code declaredClass}, and
	 *         {@code fieldName}
	 * @exception NoSuchFieldException
	 */
	public static Field declaredFieldFor(final Class<?> declaringClass,
			final String fieldName) throws SecurityException,
			NoSuchFieldException {
		if (System.getSecurityManager() != null) {
			try {
				return AccessController
						.doPrivileged(new PrivilegedExceptionAction<Field>() {
							public Field run() throws SecurityException,
									NoSuchFieldException {
								return Primitives.basicDeclaredFieldFor(
										declaringClass, fieldName);
							}
						});
			} catch (PrivilegedActionException ex) {
				Throwable rootCause = ex.getCause();
				if (rootCause instanceof NoSuchFieldException) {
					throw (NoSuchFieldException) rootCause;
				} else if (rootCause instanceof SecurityException) {
					throw (SecurityException) rootCause;
				} else if (rootCause instanceof RuntimeException) {
					throw (RuntimeException) rootCause;
				}
				// TODO
				ex.printStackTrace();
			}
		}
		return Primitives.basicDeclaredFieldFor(declaringClass, fieldName);
	}

	protected static boolean matchParameterTypes(
			Class<?>[] declaredParameterTypes,
			Class<?>[] requestedParameterTypes) {
		int declaredParameterCount = declaredParameterTypes.length;
		int requestedParameterCount;
		if (requestedParameterTypes != null) {
			requestedParameterCount = requestedParameterTypes.length;
		} else {
			requestedParameterCount = 0;
		}
		if (declaredParameterCount == requestedParameterCount) {
			for (int i = 0;; i++) {
				if (i >= declaredParameterCount) {
					return true;
				}
				Class<?> requestedType = requestedParameterTypes[i];
				if (requestedType != null) {
					Class<?> declaredType = declaredParameterTypes[i];
					if (!declaredType.isAssignableFrom(requestedType)) {
						if (declaredType.isPrimitive()) {
							try {
								Field typeField = requestedType
										.getField("TYPE");
								requestedType = (Class<?>) typeField.get(null);
								if (declaredType != requestedType) {
									break;
								}
							} catch (Exception ex) {
								break;
							}
						} else {
							break;
						}
					}
				} else {
					break;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected static <T> Constructor<T> suitableConstructorFor(
			Class<T> declaringClass, Class<?>[] parameterTypes) {
		Constructor<?>[] constructors = declaringClass
				.getDeclaredConstructors();
		if (constructors.length == 0) {
			return null;
		}
		int matchCount = 0;
		for (int i = 0; i < constructors.length; ++i) {
			Constructor<?> candidateConstructor = constructors[i];
			Class<?>[] candidateParameterTypes = candidateConstructor
					.getParameterTypes();
			if (Primitives.matchParameterTypes(candidateParameterTypes,
					parameterTypes)) {
				int gap = -1;
				boolean electCandidate = true;
				for (int j = 0; j < matchCount; j++) {
					Constructor<?> electedConstructor = constructors[j];
					if (gap != -1) {
						constructors[gap++] = electedConstructor;
					}
					Class<?>[] electedTypes = electedConstructor
							.getParameterTypes();
					if (Primitives.matchParameterTypes(electedTypes,
							candidateParameterTypes)) {
						gap = j;
					} else if (Primitives.matchParameterTypes(
							candidateParameterTypes, electedTypes)) {
						electCandidate = false;
					}
				}
				if (gap != -1) {
					matchCount = gap;
				}
				if (electCandidate) {
					constructors[matchCount++] = candidateConstructor;
				}
			}
		}
		return matchCount == 1 ? (Constructor<T>) constructors[0] : null;
	}

	public static <T> Constructor<T> declaredConstructorFor(
			Class<T> declaringClass, Class<?>[] parameterTypes)
			throws NoSuchMethodException {
		try {
			Constructor<T> constructor = declaringClass
					.getDeclaredConstructor(parameterTypes);
			constructor.setAccessible(true);
			return constructor;
		} catch (NoSuchMethodException ex) {
			Constructor<T> constructor = Primitives.suitableConstructorFor(
					declaringClass, parameterTypes);
			if (constructor != null) {
				constructor.setAccessible(true);
				return constructor;
			}
			throw ex;
		}
	}

	protected static Method suitableMethodFor(Class<?> declaringClass,
			String methodName, Class<?>[] argumentTypes) {
		Method[] declaredMethods = declaringClass.getDeclaredMethods();
		if (declaredMethods.length == 0) {
			return null;
		}
		int matchCount = 0;
		for (int i = 0; i < declaredMethods.length; ++i) {
			Method candidateMethod = declaredMethods[i];
			if (candidateMethod.getName().equals(methodName)) {
				Class<?>[] candidateParameterTypes = candidateMethod
						.getParameterTypes();
				if (Primitives.matchParameterTypes(candidateParameterTypes,
						argumentTypes)) {
					int gap = -1;
					boolean electProspect = true;
					for (int j = 0; j < matchCount; j++) {
						Method electedMethod = declaredMethods[j];
						if (gap != -1) {
							declaredMethods[gap++] = electedMethod;
						}
						Class<?>[] electedTypes = electedMethod
								.getParameterTypes();
						if (Primitives.matchParameterTypes(electedTypes,
								candidateParameterTypes)) {
							gap = j;
						} else if (Primitives.matchParameterTypes(
								candidateParameterTypes, electedTypes)) {
							electProspect = false;
						}
					}
					if (gap != -1) {
						matchCount = gap;
					}
					if (electProspect) {
						declaredMethods[matchCount++] = candidateMethod;
					}
				}
			}
		}
		return matchCount == 1 ? declaredMethods[0] : null;
	}

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied
	 * name and parameter types. Searches all superclasses up to
	 * <code>Object</code>.
	 * <p>
	 * Returns <code>null</code> if no {@link Method} can be found.
	 * 
	 * @param clazz
	 *            the class to introspect
	 * @param name
	 *            the name of the method
	 * @param paramTypes
	 *            the parameter types of the method
	 * @return the Method object, or <code>null</code> if none found
	 */
	public static Method findMethod(Class<?> clazz, String name,
			Class<?>[] paramTypes) {
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Method[] methods = searchType.isInterface() ? searchType
					.getMethods() : searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (name.equals(method.getName())
						&& Arrays
								.equals(paramTypes, method.getParameterTypes())) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * Search for the generic {@link Method} declaration whose erased signature
	 * matches that of the supplied bridge method.
	 * 
	 * @throws IllegalStateException
	 *             if the generic declaration cannot be found
	 */
	public static Method findGenericDeclaration(Method bridgeMethod) {
		// Search parent types for method that has same signature as bridge.
		Class<?> superclass = bridgeMethod.getDeclaringClass().getSuperclass();
		while (!Object.class.equals(superclass)) {
			Method method = Primitives.searchForMatch(superclass, bridgeMethod);
			if (method != null && !method.isBridge()) {
				return method;
			}
			superclass = superclass.getSuperclass();
		}

		// Search interfaces.
		Class<?>[] interfaces = Primitives.allInterfacesOf(bridgeMethod
				.getDeclaringClass());
		for (Class<?> anInterface : interfaces) {
			Method method = Primitives
					.searchForMatch(anInterface, bridgeMethod);
			if (method != null && !method.isBridge()) {
				return method;
			}
		}

		return null;
	}

	/**
	 * Determine the raw type for the given generic parameter type.
	 */
	private static Type getRawType(Type genericType,
			Map<TypeVariable<?>, Type> typeVariableMap) {
		if (genericType instanceof TypeVariable) {
			TypeVariable<?> tv = (TypeVariable<?>) genericType;
			Type result = typeVariableMap.get(tv);
			return result != null ? result : Object.class;
		} else if (genericType instanceof ParameterizedType) {
			return ((ParameterizedType) genericType).getRawType();
		} else {
			return genericType;
		}
	}

	/**
	 * Return <code>true</code> if the {@link Type} signature of both the
	 * supplied {@link Method#getGenericParameterTypes() generic Method} and
	 * concrete {@link Method} are equal after resolving all
	 * {@link TypeVariable TypeVariables} using the supplied
	 * {@link #createTypeVariableMap TypeVariable Map}, otherwise returns
	 * <code>false</code>.
	 */
	public static boolean isResolvedTypeMatch(Method genericMethod,
			Method candidateMethod, Map<TypeVariable<?>, Type> typeVariableMap) {
		Type[] genericParameters = genericMethod.getGenericParameterTypes();
		Class<?>[] candidateParameters = candidateMethod.getParameterTypes();
		if (genericParameters.length != candidateParameters.length) {
			return false;
		}
		for (int i = 0; i < genericParameters.length; i++) {
			Type genericParameter = genericParameters[i];
			Class<?> candidateParameter = candidateParameters[i];
			if (candidateParameter.isArray()) {
				// An array type: compare the component type.
				Type rawType = Primitives.getRawType(genericParameter,
						typeVariableMap);
				if (rawType instanceof GenericArrayType) {
					if (!candidateParameter.getComponentType().equals(
							Primitives
									.getRawType(((GenericArrayType) rawType)
											.getGenericComponentType(),
											typeVariableMap))) {
						return false;
					}
					break;
				}
			}
			// A non-array type: compare the type itself.
			if (!candidateParameter.equals(Primitives.getRawType(
					genericParameter, typeVariableMap))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return <code>true</code> if the supplied '<code>candidateMethod</code>'
	 * can be consider a validate candidate for the {@link Method} that is
	 * {@link Method#isBridge() bridged} by the supplied {@link Method bridge
	 * Method}. This method performs inexpensive checks and can be used quickly
	 * filter for a set of possible matches.
	 */
	public static boolean isBridgedCandidateFor(Method candidateMethod,
			Method bridgeMethod) {
		return !candidateMethod.isBridge()
				&& !candidateMethod.equals(bridgeMethod)
				&& candidateMethod.getName().equals(bridgeMethod.getName())
				&& candidateMethod.getParameterTypes().length == bridgeMethod
						.getParameterTypes().length;
	}

	/**
	 * Determine whether or not the bridge {@link Method} is the bridge for the
	 * supplied candidate {@link Method}.
	 */
	public static boolean isBridgeMethodFor(Method bridgeMethod,
			Method candidateMethod, Map<TypeVariable<?>, Type> typeVariableMap) {
		if (Primitives.isResolvedTypeMatch(candidateMethod, bridgeMethod,
				typeVariableMap)) {
			return true;
		}
		Method method = Primitives.findGenericDeclaration(bridgeMethod);
		return method != null ? Primitives.isResolvedTypeMatch(method,
				candidateMethod, typeVariableMap) : false;
	}

	public static Map<TypeVariable<?>, Type> createTypeVariableMap(Class<?> cls) {
		Map<TypeVariable<?>, Type> typeVariableMap = new HashMap<TypeVariable<?>, Type>();

		// interfaces
		Primitives.extractTypeVariablesFromGenericInterfaces(cls
				.getGenericInterfaces(), typeVariableMap);

		// super class
		Type genericType = cls.getGenericSuperclass();
		Class<?> type = cls.getSuperclass();
		while (!Object.class.equals(type)) {
			if (genericType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericType;
				Primitives.populateTypeMapFromParameterizedType(pt,
						typeVariableMap);
			}
			Primitives.extractTypeVariablesFromGenericInterfaces(type
					.getGenericInterfaces(), typeVariableMap);
			genericType = type.getGenericSuperclass();
			type = type.getSuperclass();
		}

		// enclosing class
		type = cls;
		while (type.isMemberClass()) {
			genericType = type.getGenericSuperclass();
			if (genericType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericType;
				Primitives.populateTypeMapFromParameterizedType(pt,
						typeVariableMap);
			}
			type = type.getEnclosingClass();
		}

		return typeVariableMap;
	}

	private static void extractTypeVariablesFromGenericInterfaces(
			Type[] genericInterfaces, Map<TypeVariable<?>, Type> typeVariableMap) {
		for (Type genericInterface : genericInterfaces) {
			if (genericInterface instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericInterface;
				Primitives.populateTypeMapFromParameterizedType(pt,
						typeVariableMap);
				if (pt.getRawType() instanceof Class) {
					Primitives
							.extractTypeVariablesFromGenericInterfaces(
									((Class<?>) pt.getRawType())
											.getGenericInterfaces(),
									typeVariableMap);
				}
			} else if (genericInterface instanceof Class) {
				Primitives.extractTypeVariablesFromGenericInterfaces(
						((Class<?>) genericInterface).getGenericInterfaces(),
						typeVariableMap);
			}
		}
	}

	/**
	 * Read the {@link TypeVariable TypeVariables} from the supplied
	 * {@link ParameterizedType} and add mappings corresponding to the
	 * {@link TypeVariable#getName TypeVariable name} -> concrete type to the
	 * supplied {@link Map}.
	 * <p>
	 * Consider this case:
	 * 
	 * <pre class="code> public interface Foo&lt;S, T&gt; { .. }
	 * 
	 * public class FooImpl implements Foo&lt;String, Integer&gt; { .. } </pre>
	 * 
	 * For '<code>FooImpl</code>' the following mappings would be added to the
	 * {@link Map}: {S=java.lang.String, T=java.lang.Integer}.
	 */
	private static void populateTypeMapFromParameterizedType(
			ParameterizedType type, Map<TypeVariable<?>, Type> typeVariableMap) {
		if (type.getRawType() instanceof Class) {
			Type[] actualTypeArguments = type.getActualTypeArguments();
			TypeVariable<?>[] typeVariables = ((Class<?>) type.getRawType())
					.getTypeParameters();
			for (int i = 0; i < actualTypeArguments.length; i++) {
				Type actualTypeArgument = actualTypeArguments[i];
				TypeVariable<?> variable = typeVariables[i];
				if (actualTypeArgument instanceof Class) {
					typeVariableMap.put(variable, actualTypeArgument);
				} else if (actualTypeArgument instanceof GenericArrayType) {
					typeVariableMap.put(variable, actualTypeArgument);
				} else if (actualTypeArgument instanceof ParameterizedType) {
					typeVariableMap.put(variable,
							((ParameterizedType) actualTypeArgument)
									.getRawType());
				} else if (actualTypeArgument instanceof TypeVariable) {
					// We have a type that is parameterized at instantiation
					// time
					// the nearest match on the bridge method will be the
					// bounded type.
					TypeVariable<?> typeVariableArgument = (TypeVariable<?>) actualTypeArgument;
					Type resolvedType = typeVariableMap
							.get(typeVariableArgument);
					if (resolvedType == null) {
						resolvedType = Primitives
								.extractClassForTypeVariable(typeVariableArgument);
					}
					if (resolvedType != null) {
						typeVariableMap.put(variable, resolvedType);
					}
				}
			}
		}
	}

	/**
	 * Extracts the bound '<code>Class</code>' for a give {@link TypeVariable}.
	 */
	private static Class<?> extractClassForTypeVariable(
			TypeVariable<?> typeVariable) {
		Type[] bounds = typeVariable.getBounds();
		Type result = null;
		if (bounds.length > 0) {
			Type bound = bounds[0];
			if (bound instanceof ParameterizedType) {
				result = ((ParameterizedType) bound).getRawType();
			} else if (bound instanceof Class) {
				result = bound;
			} else if (bound instanceof TypeVariable) {
				result = Primitives
						.extractClassForTypeVariable((TypeVariable<?>) bound);
			}
		}
		return result instanceof Class ? (Class<?>) result : null;
	}

	/**
	 * Search for the bridged method in the given candidates.
	 * 
	 * @param candidateMethods
	 *            the List of candidate Methods
	 * @param bridgeMethod
	 *            the bridge method
	 * @return the bridged method, or <code>null</code> if none found
	 */
	private static Method searchCandidates(List<Method> candidateMethods,
			Method bridgeMethod) {
		Map<TypeVariable<?>, Type> typeParameterMap = Primitives
				.createTypeVariableMap(bridgeMethod.getDeclaringClass());
		for (int i = 0; i < candidateMethods.size(); i++) {
			Method candidateMethod = candidateMethods.get(i);
			if (Primitives.isBridgeMethodFor(bridgeMethod, candidateMethod,
					typeParameterMap)) {
				return candidateMethod;
			}
		}
		return null;
	}

	/**
	 * Find the original method for the supplied {@link Method bridge Method}.
	 * <p>
	 * It is safe to call this method passing in a non-bridge {@link Method}
	 * instance. In such a case, the supplied {@link Method} instance is
	 * returned directly to the caller. Callers are <strong>not</strong>
	 * required to check for bridging before calling this method.
	 * 
	 * @throws IllegalStateException
	 *             if no bridged {@link Method} can be found
	 */
	public static Method findBridgedMethod(Method bridgeMethod) {
		if (!bridgeMethod.isBridge()) {
			return bridgeMethod;
		}

		// Gather all methods with matching name and parameter size.
		List<Method> candidateMethods = new ArrayList<Method>();
		Method[] methods = Primitives.allDeclaredMethodsOf(bridgeMethod
				.getDeclaringClass());
		for (Method candidateMethod : methods) {
			if (Primitives.isBridgedCandidateFor(candidateMethod, bridgeMethod)) {
				candidateMethods.add(candidateMethod);
			}
		}

		Method result;
		// Now perform simple quick checks.
		if (candidateMethods.size() == 1) {
			result = candidateMethods.get(0);
		} else {
			result = Primitives
					.searchCandidates(candidateMethods, bridgeMethod);
		}

		// if (result == null) {
		// throw new IllegalStateException(
		// "Unable to locate bridged method for bridge method '"
		// + bridgeMethod + "'");
		// }

		return result;
	}

	/**
	 * If the supplied {@link Class} has a declared {@link Method} whose
	 * signature matches that of the supplied {@link Method}, then this matching
	 * {@link Method} is returned, otherwise <code>null</code> is returned.
	 */
	public static Method searchForMatch(Class<?> type, Method bridgeMethod) {
		return Primitives.findMethod(type, bridgeMethod.getName(), bridgeMethod
				.getParameterTypes());
	}

	protected static Method basicDeclaredMethod(Class<?> declaringClass,
			String methodName, Class<?>[] argumentTypes)
			throws NoSuchMethodException {
		NoSuchMethodException exception = null;
		Class<?> currentClass = declaringClass;
		while (currentClass != null) {
			try {
				Method declaredMethod = declaringClass.getDeclaredMethod(
						methodName, argumentTypes);
				declaredMethod.setAccessible(true);
				return declaredMethod;
			} catch (NoSuchMethodException ex) {
				if (exception == null) {
					exception = ex;
				}
				Method suitableMethod = Primitives.suitableMethodFor(
						declaringClass, methodName, argumentTypes);
				if (suitableMethod != null) {
					suitableMethod.setAccessible(true);
					return suitableMethod;
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		if (exception != null) {
			throw exception;
		}
		return null;
	}

	/**
	 * Returns the declared method for the given {@code declaredClass}, {@code
	 * methodName}, and {@code parameterTypes}. Requires security policy
	 * 'permission java.lang.RuntimePermission "accessDeclaredMembers";'
	 * 
	 * @return Method the declared method for the given {@code declaredClass},
	 *         {@code methodName}, and {@code parameterTypes}
	 * @exception NoSuchMethodException
	 */
	public static Method declaredMethodFor(final Class<?> declaringClass,
			final String methodName, final Class<?>[] parameterTypes)
			throws SecurityException, NoSuchMethodException {
		if (System.getSecurityManager() != null) {
			try {
				return AccessController
						.doPrivileged(new PrivilegedExceptionAction<Method>() {
							public Method run() throws SecurityException,
									NoSuchMethodException {
								return Primitives.basicDeclaredMethod(
										declaringClass, methodName,
										parameterTypes);
							}
						});
			} catch (PrivilegedActionException ex) {
				Throwable rootCause = ex.getCause();
				if (rootCause instanceof NoSuchMethodException) {
					throw (NoSuchMethodException) rootCause;
				} else if (rootCause instanceof SecurityException) {
					throw (SecurityException) rootCause;
				} else if (rootCause instanceof RuntimeException) {
					throw (RuntimeException) rootCause;
				}
				// TODO
				ex.printStackTrace();
			}
		}
		return Primitives.basicDeclaredMethod(declaringClass, methodName,
				parameterTypes);
	}

	/**
	 * Returns the declared methods for the given {@code declaredClass}.
	 * Requires security policy: 'permission java.lang.RuntimePermission
	 * "accessDeclaredMembers";'
	 * 
	 * @return Method[] the declared methods for the given {@code declaredClass}
	 */
	public static Method[] declaredMethodsOf(final Class<?> declaringClass)
			throws SecurityException {
		if (System.getSecurityManager() != null) {
			return AccessController
					.doPrivileged(new PrivilegedAction<Method[]>() {
						public Method[] run() {
							if (declaringClass.isInterface()) {
								return declaringClass.getMethods();
							}
							return declaringClass.getDeclaredMethods();
						}
					});
		}
		if (declaringClass.isInterface()) {
			return declaringClass.getMethods();
		}
		return declaringClass.getDeclaredMethods();
	}

	public static Method[] allDeclaredMethodsOf(Class<?> declaringClass) {
		List<Method> declaredMethods = new ArrayList<Method>();
		while (declaringClass != null) {
			for (Method method : Primitives.declaredMethodsOf(declaringClass)) {
				declaredMethods.add(method);
			}
			declaringClass = declaringClass.getSuperclass();
		}
		return declaredMethods.toArray(new Method[declaredMethods.size()]);
	}

	/**
	 * Make the given field accessible, explicitly setting it accessible if
	 * necessary, to avoid unnecessary conflicts with a JVM SecurityManager (if
	 * active).
	 * 
	 * @param field
	 *            the field to make accessible
	 * @see java.lang.reflect.Field#setAccessible
	 */
	public static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * Make the given method accessible, explicitly setting it accessible if
	 * necessary, to avoid unnecessary conflicts with a JVM SecurityManager (if
	 * active).
	 * 
	 * @param method
	 *            the method to make accessible
	 * @see java.lang.reflect.Method#setAccessible
	 */
	public static void makeAccessible(Method method) {
		if (!Modifier.isPublic(method.getModifiers())
				|| !Modifier
						.isPublic(method.getDeclaringClass().getModifiers())) {
			method.setAccessible(true);
		}
	}

	/**
	 * Make the given constructor accessible, explicitly setting it accessible
	 * if necessary, to avoid unnecessary conflicts with a JVM SecurityManager
	 * (if active).
	 * 
	 * @param constructor
	 *            the constructor to make accessible
	 * @see java.lang.reflect.Constructor#setAccessible
	 */
	public static void makeAccessible(Constructor<?> constructor) {
		if (!Modifier.isPublic(constructor.getModifiers())
				|| !Modifier.isPublic(constructor.getDeclaringClass()
						.getModifiers())) {
			constructor.setAccessible(true);
		}
	}

	/**
	 * Returns the class name for the given <code>type</code>.
	 * 
	 * @param type
	 *            the class to get the name for; must not be null
	 * @return the class name for the given <code>type</code>
	 * @see Class#getName()
	 */
	public static String qualifiedClassNameOf(Class<?> type) {
		return Primitives.mustNotBeNull("type", type).getName();
	}

	/**
	 * Returns the package name of the given <code>type</code>.
	 * 
	 * @param type
	 *            the class to get the package name for; must not be null
	 * @return the package name of the given <code>type</code>
	 */
	public static String packageNameOf(Class<?> type) {
		Primitives.mustNotBeNull("type", type);
		if (type.isPrimitive()) {
			return "";
		}
		Package packageOfType = type.getPackage();
		if (packageOfType != null) {
			return packageOfType.getName();
		}
		String qualifiedClassName = type.getName();
		return qualifiedClassName.substring(0, qualifiedClassName
				.lastIndexOf('.'));
	}

	/**
	 * Returns the <i>short</i> class name without the package name for the
	 * given <code>type</code>.
	 * 
	 * @param type
	 *            the class to get the short name for; must not be null
	 * @return the <i>short</i> class name without the package name for the
	 *         given <code>type</code>
	 */
	public static String classNameOf(Class<?> type) {
		return Primitives.mustNotBeNull("type", type).getSimpleName();
	}

	/**
	 * Returns the raw type of the given generic type. Instances of Class will
	 * be returned unchanged. For instances of ParameterizedType the raw type
	 * will be returned. For instances of TypeVariable the raw type of the first
	 * bound will be returned. For instances of GenericArrayType a Class
	 * representing an Array of the raw component type of the GenericArrayType
	 * will be returned. This method is not applicable for instances of
	 * WildcardType.
	 * 
	 * @param type
	 *            the type for which the raw type should be returned
	 * @return the raw type for the given generic type
	 */
	public static Class<?> getRawType(Type type) {
		if (type instanceof Class) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			return (Class<?>) ((ParameterizedType) type).getRawType();
		} else if (type instanceof TypeVariable) {
			return Primitives
					.getRawType(((TypeVariable<?>) type).getBounds()[0]);
		} else if (type instanceof GenericArrayType) {
			Type compType = ((GenericArrayType) type).getGenericComponentType();
			return Array.newInstance(Primitives.getRawType(compType), 0)
					.getClass();
		} else {
			// TODO
			throw new RuntimeException("Unable to extract raw type from "
					+ type);
		}
	}

	public static <T extends Annotation> T annotationFrom(
			Class<T> annotationClass, Class<?> type) {
		Class<?> currentType = type;
		while (currentType != null) {
			T annotation = currentType.getAnnotation(annotationClass);
			if (annotation != null) {
				return annotation;
			}
			currentType = currentType.getSuperclass();
		}
		return null;
	}

	/**
	 * Returns the declared fields of the given {@code type} that are marked
	 * with the given {@code annotationClass}.
	 * 
	 * @param annotationClass
	 *            the annotation to be searched for, not null
	 * @param type
	 *            the class to be inspected.
	 * @return a {@link List} containing fields annotated with the given {@code
	 *         annotationClass}, empty list if none found
	 */
	public static <T extends Annotation> List<Field> annotatedFieldsFrom(
			Class<T> annotationClass, Class<?> type) {
		Class<?> currentType = type;
		List<Field> annotatedFields = new ArrayList<Field>();
		while (currentType != null) {
			for (Field field : currentType.getDeclaredFields()) {
				if (field.isAnnotationPresent(annotationClass)) {
					annotatedFields.add(field);
				}
			}
			currentType = currentType.getSuperclass();
		}
		return annotatedFields;
	}

	/**
	 * Returns the declared methods of the given {@code type} that are marked
	 * with the given {@code annotationClass}.
	 * 
	 * @param annotationClass
	 *            the annotation to be searched for, not null
	 * @param type
	 *            the class to be inspected.
	 * @return a {@link List} containing methods annotated with the given
	 *         {@code annotationClass}, empty list if none found
	 */
	public static <T extends Annotation> List<Method> annotatedMethodsFrom(
			Class<T> annotationClass, Class<?> type) {
		Class<?> currentType = type;
		List<Method> annotatedMethods = new ArrayList<Method>();
		while (currentType != null) {
			for (Method method : currentType.getDeclaredMethods()) {
				if (method.isAnnotationPresent(annotationClass)) {
					annotatedMethods.add(method);
				}
			}
			currentType = currentType.getSuperclass();
		}
		return annotatedMethods;
	}

	/**
	 * Return the declared {@link Annotation}s for the given {@code
	 * annotatedElement}. Requires security policy: 'permission
	 * java.lang.RuntimePermission "accessDeclaredMembers";'
	 * 
	 * @return the declared {@link Annotation}s for the given {@code
	 *         annotatedElement}
	 */
	public static Annotation[] declaredAnnotationsFor(
			final AnnotatedElement annotatedElement) {
		if (System.getSecurityManager() != null) {
			return AccessController
					.doPrivileged(new PrivilegedAction<Annotation[]>() {
						public Annotation[] run() {
							return annotatedElement.getDeclaredAnnotations();
						}
					});
		}
		return annotatedElement.getDeclaredAnnotations();
	}

	/**
	 * Returns an array of {@link Type} objects representing the actual type
	 * arguments to given {@code type}.
	 * 
	 * @param type
	 *            the class object to be inspected
	 * @return an array of {@link Type} objects representing the actual type
	 *         arguments to given {@code type}
	 */
	public static Type[] actualTypeArgumentsOf(Class<?> type) {
		Type genericSuperclass = type.getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
			return parameterizedType.getActualTypeArguments();
		}
		return Primitives.EMPTY_TYPE_ARRAY;
	}

	static {
		Primitives.PRIMITIVE_WRAPPER_LOOKUP.put(Boolean.class, boolean.class);
		Primitives.PRIMITIVE_WRAPPER_LOOKUP.put(Byte.class, byte.class);
		Primitives.PRIMITIVE_WRAPPER_LOOKUP.put(Character.class, char.class);
		Primitives.PRIMITIVE_WRAPPER_LOOKUP.put(Double.class, double.class);
		Primitives.PRIMITIVE_WRAPPER_LOOKUP.put(Float.class, float.class);
		Primitives.PRIMITIVE_WRAPPER_LOOKUP.put(Integer.class, int.class);
		Primitives.PRIMITIVE_WRAPPER_LOOKUP.put(Long.class, long.class);
		Primitives.PRIMITIVE_WRAPPER_LOOKUP.put(Short.class, short.class);
	}

}
