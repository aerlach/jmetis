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
package org.jmetis.reflection.property;

import java.lang.reflect.Field;

import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code FieldAccessor}
 * 
 * @author aerlach
 */
public class FieldAccessor extends PropertyAccessor {

	private Field field;

	/**
	 * Constructs a new {@code FieldAccessor} instance.
	 */
	public FieldAccessor(IPropertyDescription propertyDescriptor, Field field) {
		super(propertyDescriptor);
		this.field = this.mustNotBeNull("field", field);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.mic.kernel.core.reflection.IPropertyAdapter#getValueFrom(java.lang
	 * .Object)
	 */
	public Object getValueFrom(Object object) {
		try {
			return field.get(object);
		} catch (Exception ex) {
			accessPropertyFailedBecauseOf(field, object, ex);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.reflection.IPropertyAccessor#isReadOnlyFor(java.lang.Object)
	 */
	public boolean isReadOnlyFor(Object object) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.mic.kernel.core.reflection.IPropertyAdapter#setValueInto(java.lang
	 * .Object, java.lang.Object)
	 */
	public void setValueInto(Object value, Object object) {
		try {
			field.set(object, value);
		} catch (Exception ex) {
			modifyPropertyFailedBecauseOf(field, object, value, ex);
		}
	}

}
