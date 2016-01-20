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


/**
 * {@code IPropertyAccessor} can be used to make a property accessible via
 * reflection.
 * <p>
 * NOTE: The property access may be provided either by using the corresponding
 * access methods or by direct field access.
 * </p>
 * 
 * @author aerlach
 */
public interface IPropertyAccessor {

	/**
	 * 
	 * @return
	 */
	IPropertyDescription propertyDescriptor();

	/**
	 * Returns the value of the property represented by this adapter from the
	 * given {@code object}. The value is automatically wrapped in an object if
	 * it has a primitive type.
	 * 
	 * @param object
	 *            the object from which the value of the property represented by
	 *            this adapter is to be extracted
	 * @return the value of the property represented by this adapter from the
	 *         given {@code object}
	 */
	Object getValueFrom(Object object);

	/**
	 * Returns {@code true} if the property cannot be written for the given
	 * {@code object}, otherwise {@code false}.
	 * 
	 * @return {@code true} if the property cannot be written for the given
	 *         {@code object}, otherwise {@code false}
	 */
	boolean isReadOnlyFor(Object object);

	/**
	 * Modifies the property represented by this adapter on the given {@code
	 * object} to the given {@code value}. The new value is automatically
	 * unwrapped if the underlying property has a primitive type.
	 * 
	 * @param object
	 *            the object whose property represented by this adapter should
	 *            be modified
	 * @param value
	 *            the new value for the property of {@code object} being
	 *            modified
	 */
	void setValueInto(Object value, Object object);

}
