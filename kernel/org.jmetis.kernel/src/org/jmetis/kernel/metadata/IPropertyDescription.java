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

import java.lang.reflect.AnnotatedElement;

/**
 * {@code IPropertyDescription} describes one property of a Java Bean.
 * <p>
 * NOTE: The property access may be provided either by using the corresponding
 * access methods or by direct field access.
 * </p>
 * 
 * @author aerlach
 */
public interface IPropertyDescription extends AnnotatedElement {

	/**
	 * Returns the {@link IClassDescription} of the declaring class.
	 * 
	 * @return the {@link IClassDescription} of the declaring class
	 */
	IClassDescription getClassDescriptor();

	/**
	 * Returns the programmatic name of the property.
	 * 
	 * @return the programmatic name of the property
	 */
	String getPropertyName();

	/**
	 * 
	 * @return
	 */
	Class<?> getPropertyType();

	IClassDescription getPropertyTypeDescriptor();

	Class<?>[] getElementTypes();

	/**
	 * 
	 * @return
	 */
	IPropertyAccessor getPropertyAccessor();

}
