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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code MethodAccessor}
 * 
 * @author aerlach
 */
public class MethodAccessor extends PropertyAccessor {

	private Method readMethod;

	private Method writeMethod;

	/**
	 * Constructs a new {@code MethodAccessor} instance.
	 */
	public MethodAccessor(IPropertyDescription propertyDescriptor,
			Method readMethod, Method writeMethod) {
		super(propertyDescriptor);
		this.readMethod = this.mustNotBeNull("readMethod", readMethod);
		this.writeMethod = writeMethod;
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
			return readMethod.invoke(object);
		} catch (InvocationTargetException ex) {
			accessPropertyFailedBecauseOf(readMethod, object, ex.getCause());
		} catch (Exception ex) {
			accessPropertyFailedBecauseOf(readMethod, object, ex);
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
		return writeMethod == null;
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
			writeMethod.invoke(object, value);
		} catch (InvocationTargetException ex) {
			modifyPropertyFailedBecauseOf(writeMethod, object, value, ex
					.getCause());
		} catch (Exception ex) {
			modifyPropertyFailedBecauseOf(writeMethod, object, value, ex);
		}
	}

}
