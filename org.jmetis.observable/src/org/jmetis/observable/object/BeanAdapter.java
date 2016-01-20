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
package org.jmetis.observable.object;

import java.lang.reflect.AnnotatedElement;

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.observable.IPropertyModel;

/**
 * {@code BeanAdapter}
 * 
 * @author aerlach
 */
public class BeanAdapter extends PropertyModel implements IPropertyModel {

	private IClassDescription classDescriptor;

	private Object value;

	/**
	 * Constructs a new {@code BeanAdapter} instance.
	 * 
	 * @param classDescriptor
	 */
	public BeanAdapter(IClassDescription classDescriptor) {
		super();
		this.classDescriptor = classDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.object.PropertyModel#getAnnotatedElement()
	 */
	@Override
	protected AnnotatedElement getAnnotatedElement() {
		return classDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.object.PropertyModel#getClassDescriptor()
	 */
	@Override
	protected IClassDescription getClassDescriptor() {
		return classDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IPropertyModel#getValueType()
	 */
	public Class<?> getValueType() {
		return classDescriptor.getDescribedClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IPropertyModel#getElementTypes()
	 */
	public Class<?>[] getElementTypes() {
		return PropertyModel.EMPTY_CLASS_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IPropertyModel#isReadOnly()
	 */
	public boolean isReadOnly() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.object.PropertyModel#getValue()
	 */
	public Object getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.object.PropertyModel#valueChanged(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	protected void valueChanged(Object oldValue, Object newValue) {
		firePropertyChangeEvent(oldValue, newValue);
		super.valueChanged(oldValue, newValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.object.IBeanVariable#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		if (this.value != value) {
			valueChanged(this.value, this.value = Assertions
					.mustBeAssignableTo("value", value, classDescriptor
							.getDescribedClass()));
		}
	}
}
