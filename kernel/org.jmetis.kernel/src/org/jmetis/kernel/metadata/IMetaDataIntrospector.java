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
package org.jmetis.kernel.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;

/**
 * {@code IMetaDataIntrospector}
 * 
 * @author aerlach
 */
public interface IMetaDataIntrospector {

	/**
	 * 
	 * @param classToDescribe
	 * @return
	 */
	IClassDescription classDescriptorOf(Class<?> classToDescribe);

	/**
	 * Returns the declared fields for the given {@code declaringClass}.
	 * <p>
	 * NOTE: Requires security policy: 'permission java.lang.RuntimePermission
	 * "accessDeclaredMembers";'
	 * </p>
	 * 
	 * @return the declared fields for the given {@code declaringClass}
	 */
	Field[] declaredFieldsOf(Class<?> declaringClass) throws SecurityException;

	/**
	 * Returns the declared field for the given {@code declaringClass}, and
	 * {@code fieldName}.
	 * <p>
	 * NOTE: Requires security policy: 'permission java.lang.RuntimePermission
	 * "accessDeclaredMembers";'
	 * </p>
	 * 
	 * @return the declared field for the given {@code declaringClass}, and
	 *         {@code fieldName}
	 * @exception NoSuchFieldException
	 */
	Field declaredFieldNamed(Class<?> declaringClass, String fieldName)
			throws SecurityException, NoSuchFieldException;

	/**
	 * Returns the declared methods for the given {@code declaringClass}.
	 * <p>
	 * NOTE: Requires security policy: 'permission java.lang.RuntimePermission
	 * "accessDeclaredMembers";'
	 * </p>
	 * 
	 * @return the declared methods for the given {@code declaringClass}
	 */
	Method[] declaredMethodsOf(Class<?> declaringClass)
			throws SecurityException;

	/**
	 * 
	 * @param declaringClass
	 * @return
	 */
	Method[] allDeclaredMethodsOf(Class<?> declaringClass);

	/**
	 * 
	 * @param declaringClass
	 * @return
	 */
	Map<String, Method[]> propertyMethodsOf(Class<?> declaringClass);

	/**
	 * Return the declared {@link Annotation}s for the given {@code
	 * annotatedElement}.
	 * <p>
	 * NOTE: Requires security policy: 'permission java.lang.RuntimePermission
	 * "accessDeclaredMembers";'
	 * </p>
	 * 
	 * @return the declared {@link Annotation}s for the given {@code
	 *         annotatedElement}
	 */
	Annotation[] declaredAnnotationsOf(AnnotatedElement annotatedElement);

	/**
	 * Returns the declared methods of the given {@code declaringClass} that are
	 * marked with the given {@code annotationClass}.
	 * 
	 * @param annotationClass
	 *            the annotation to be searched for, not null
	 * @param declaringClass
	 *            the class to be inspected.
	 * @return a {@link List} containing methods annotated with the given
	 *         {@code annotationClass}, empty list if none found
	 */
	<T extends Annotation> List<Method> annotatedMethodsFrom(
			Class<T> annotationClass, Class<?> declaringClass);

	/**
	 * Returns an array of {@link Type} objects representing the actual type
	 * arguments to given {@code type}.
	 * 
	 * @param type
	 *            the type object to be inspected
	 * @return an array of {@link Type} objects representing the actual type
	 *         arguments to given {@code type}
	 */
	Type[] actualTypeArgumentsOf(Type type);

	Map<TypeVariable<?>, Type> typeVariableMapOf(Class<?> classToDescribe);

}
