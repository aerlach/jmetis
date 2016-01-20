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

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.kernel.metadata.IPropertyAccessor;
import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code PropertyAccessorDecorator}
 * 
 * @author aerlach
 */
public class PropertyAccessorDecorator implements IPropertyAccessor {

	private IPropertyAccessor decoratedProperty;

	/**
	 * Constructs a new {@code PropertyAccessorDecorator} instance.
	 * 
	 */
	public PropertyAccessorDecorator(IPropertyAccessor decoratedProperty) {
		super();
		this.decoratedProperty = this.mustNotBeNull("decoratedProperty",
				decoratedProperty);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Assertions#mustNotBeNull(String, Object)
	 */
	protected <T> T mustNotBeNull(String name, T value) {
		return Assertions.mustNotBeNull(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IPropertyAccessor#getPropertyDescriptor()
	 */
	public IPropertyDescription propertyDescriptor() {
		return decoratedProperty.propertyDescriptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IPropertyAccessor#getValueFrom(java.lang
	 * .Object)
	 */
	public Object getValueFrom(Object object) {
		return decoratedProperty.getValueFrom(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IPropertyAccessor#isReadOnlyFor(java.lang
	 * .Object)
	 */
	public boolean isReadOnlyFor(Object object) {
		return decoratedProperty.isReadOnlyFor(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IPropertyAccessor#setValueInto(java.lang
	 * .Object, java.lang.Object)
	 */
	public void setValueInto(Object value, Object object) {
		decoratedProperty.setValueInto(value, object);
	}

}
