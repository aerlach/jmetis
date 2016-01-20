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
package org.jmetis.kernel.factory;

import java.lang.reflect.Constructor;
import java.security.AccessController;

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.kernel.exception.IExceptionHandler;

/**
 * {@code ObjectFactory} is a generic factory implementation.
 * 
 * @author era
 */
public class ObjectFactory<T> implements IObjectFactory<T> {

	private static final Class<?>[] PRIMITIVE_TYPES = new Class[] {
			Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
			Float.TYPE, Double.TYPE };

	private final Class<T> objectType;

	private final IObjectInitializer<T>[] objectInitializers;

	Constructor<T> constructor;

	/**
	 * Constructs a new {@code ObjectFactory} instance.
	 */
	public ObjectFactory(Class<T> objectType,
			IObjectInitializer<T>... objectInitializers) {
		super();
		this.objectType = Assertions.mustNotBeNull("objectType", objectType);
		this.objectInitializers = objectInitializers;
		this.constructor = this.createConstructor();
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<T> getObjectType() {
		return this.objectType;
	}

	protected IExceptionHandler exceptionHandler() {
		return IExceptionHandler.DEFAULT_INSTANCE;
	}

	/**
	 *Tests whether the type represented by the given <code>sourceType</code>
	 * parameter can be converted to the type of the given
	 * <code>targetType</code> parameter via an identity conversion or via a
	 * widening reference conversion. See <em>The Java Language
	 * Specification</em>, sections 5.1.1 and 5.1.4 , for details. The widening
	 * primitives case is also tested. NOTE: void can only convert to void,
	 * boolean can only convert to boolean, numeric which are sequenced and char
	 * which inserts itself into the numerics by promoting to int or larger.
	 */
	protected boolean matchArgumentType(Class<?> targetType, Class<?> sourceType) {
		if (targetType.equals(sourceType)) {
			return true;
		}
		if (sourceType == null) {
			return !targetType.isPrimitive();
		}
		while (targetType.isArray()) {
			if (!sourceType.isArray()) {
				return false;
			} else {
				targetType = targetType.getComponentType();
				sourceType = sourceType.getComponentType();
			}
		}
		if (sourceType.isArray()) {
			return false;
		}
		if (targetType.isAssignableFrom(sourceType)) {
			return true;
		}
		if (targetType.equals(Void.TYPE) || targetType.equals(Boolean.TYPE)
				|| sourceType.equals(Void.TYPE)
				|| sourceType.equals(Boolean.TYPE)) {
			return false;
		}
		for (int i = 0, n = ObjectFactory.PRIMITIVE_TYPES.length; i < n; i++) {
			if (targetType.equals(ObjectFactory.PRIMITIVE_TYPES[i])) {
				for (int j = 0; j < ObjectFactory.PRIMITIVE_TYPES.length; j++) {
					if (sourceType.equals(ObjectFactory.PRIMITIVE_TYPES[j])) {
						return j < i && (j != 0 || i > 2);
					}
				}
			}
		}
		return sourceType.equals(Integer.TYPE)
				&& (targetType.equals(Byte.TYPE)
						|| targetType.equals(Short.TYPE) || targetType
						.equals(Character.TYPE));
	}

	protected boolean matchArgumentTypes(Class<?>[] targetTypes,
			Class<?>[] sourceTypes) {
		if (targetTypes.length != sourceTypes.length) {
			return false;
		}
		for (int i = 0; i < targetTypes.length; i++) {
			if (!this.matchArgumentType(targetTypes[i], sourceTypes[i])) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	protected Constructor<T> createMatchingConstructor(Class<?>[] argumentTypes) {
		Constructor<?>[] declaredConstructors = this.objectType
				.getDeclaredConstructors();
		int matchCount = 0;
		for (int i = 0, n = declaredConstructors.length; i < n; ++i) {
			Constructor<?> prospectConstructor = declaredConstructors[i];
			Class<?>[] prospectTypes = prospectConstructor.getParameterTypes();
			if (this.matchArgumentTypes(prospectTypes, argumentTypes)) {
				int gap = -1;
				boolean electProspect = true;
				for (int j = 0; j < matchCount; j++) {
					Constructor<?> electedConstructor = declaredConstructors[j];
					if (gap != -1) {
						declaredConstructors[gap++] = electedConstructor;
					}
					Class<?>[] electedTypes = electedConstructor
							.getParameterTypes();
					if (this.matchArgumentTypes(electedTypes, prospectTypes)) {
						gap = j;
					} else if (this.matchArgumentTypes(prospectTypes,
							electedTypes)) {
						electProspect = false;
					}
				}
				if (gap != -1) {
					matchCount = gap;
				}
				if (electProspect) {
					declaredConstructors[matchCount++] = prospectConstructor;
				}
			}
		}
		if (matchCount == 1) {
			return (Constructor<T>) declaredConstructors[0];
		}
		return null;
	}

	/**
	 * Corrected Method Lookup Algorithm (see Clarifications and Amendments to
	 * the JLS)
	 * 
	 * The dynamic method lookup uses the following procedure to search class S,
	 * and then the super classes of class S, as necessary, for method m. Let X
	 * be the type of the target reference of the method invocation. <LI>If
	 * class S contains a declaration for a non-abstract method named m with the
	 * same descriptor (same number of parameters, the same parameter types, and
	 * the same return type) required by the method invocation as determined at
	 * compile time (&sect;15.11.3), then :</LI>
	 * <UL>
	 * <UL>
	 * <LI>If the invocation mode is <TT>super</TT> or <TT>interface</TT>, then
	 * this is the method to be invoked, and then procedure terminates.</LI>
	 * <LI>If the invocation mode is <TT>virtual</TT>, and the declaration in S
	 * overrides (&sect;8.4.6.1) X.m, then this is the method to be invoked, and
	 * the procedure terminates.</LI>
	 * </UL>
	 * <LI>Otherwise, if S has a superclass, this same lookup procedure is
	 * performed recursively using the direct superclass of S in place of S; the
	 * method to be invoked is the result of the recursive invocation of this
	 * lookup procedure.</LI> <LI>Otherwise, an AbstractMethodError is raised.</LI>
	 * </UL>
	 */
	protected Constructor<T> createConstructor(Class<?>[] argumentTypes) {
		Constructor<T> constructor;
		try {
			constructor = this.objectType.getDeclaredConstructor(argumentTypes);
		} catch (NoSuchMethodException ex) {
			constructor = this.createMatchingConstructor(argumentTypes);
			if (constructor == null) {
				this.exceptionHandler().handleException(ex);
			}
		}
		constructor.setAccessible(true);
		return constructor;
	}

	protected Constructor<T> createConstructor() {
		try {
			Constructor<T> constructor = this.objectType
					.getDeclaredConstructor();
			if (!constructor.isAccessible()) {
				constructor.setAccessible(true);
			}
			return constructor;
		} catch (SecurityException ex) {
			this.exceptionHandler().handleException(ex);
		} catch (NoSuchMethodException ex) {
			this.exceptionHandler().handleException(ex);
		}
		return null;
	}

	public T createInstance(Object... arguments) {
		T instance;
		try {
			if (System.getSecurityManager() != null) {
				instance = AccessController
						.doPrivileged(new PrivilegedObjectFactory<T>(this));
			}
			instance = this.constructor.newInstance();
			for (IObjectInitializer<T> initializer : this.objectInitializers) {
				initializer.initialise(instance);
			}
			return instance;
		} catch (Exception ex) {
			return this.exceptionHandler().handleException(ex, this.objectType);
		}
	}

}
