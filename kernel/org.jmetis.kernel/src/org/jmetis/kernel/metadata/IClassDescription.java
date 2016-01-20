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

/**
 * {@code IClassDescription}
 * 
 * @author aerlach
 */
public interface IClassDescription extends AnnotatedElement {

	Annotation[] EMPTY_ANNOTATION_ARRAY = {};

	String[] EMPTY_PROPERTY_NAME_ARRAY = {};

	IPropertyDescription[] EMPTY_PROPERTY_DESCRIPTOR_ARRAY = {};

	/**
	 * Returns the described {@link Class} object.
	 * 
	 * @return the described {@link Class} object
	 */
	Class<?> getDescribedClass();

	/**
	 * Returns {@code true} if the described class has a superclass; otherwise
	 * {@code false}.
	 * 
	 * @return {@code true} if the described class has a superclass; otherwise
	 *         {@code false}
	 */
	boolean hasSuperclassDescriptor();

	/**
	 * Returns the {@code IClassDescription} of the superclass or {@code null} if
	 * the superclass is null.
	 * 
	 * @return the {@code IClassDescription} of the superclass or {@code null} if
	 *         the superclass is null
	 */
	IClassDescription getSuperclassDescriptor();

	/**
	 * Returns {@code true} if the described class has a property with the given
	 * {@code propertyName}; otherwise {@code false}.
	 * 
	 *@param propertyName
	 *            the property name; must not be {@code null}
	 * @return {@code true} if the described class has a property with the given
	 *         {@code propertyName}; otherwise {@code false}
	 */
	boolean hasPropertyNamed(String propertyName);

	/**
	 * Returns the {@link IPropertyDescription} for the given {@code
	 * propertyName}.
	 * 
	 * @param propertyName
	 *            the property name; must not be {@code null}
	 * @return the {@link IPropertyDescription} for the given {@code
	 *         propertyName}
	 */
	IPropertyDescription getPropertyDescriptorNamed(String propertyName);

	/**
	 * Returns an array of the property names of the properties supported by the
	 * described class.
	 * 
	 * @return an array of {@link IPropertyDescription}s describing the
	 *         properties supported by the described class
	 */
	String[] allPropertyNames();

	/**
	 * Returns an array of {@link IPropertyDescription}s describing the
	 * properties supported by the described class.
	 * 
	 * @return an array of {@link IPropertyDescription}s describing the
	 *         properties supported by the described class
	 */
	IPropertyDescription[] allPropertyDescriptors();

}
