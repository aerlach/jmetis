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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.kernel.metadata.IPropertyAccessor;
import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code PropertyAdapter}
 * 
 * @author aerlach
 */
public class PropertyAdapter extends PropertyModel implements
		PropertyChangeListener {

	private PropertyModel propertyModel;

	private IPropertyDescription propertyDescriptor;

	private IPropertyAccessor propertyAccessor;

	/**
	 * Constructs a new {@code PropertyAdapter} instance.
	 */
	public PropertyAdapter(PropertyModel propertyModel,
			IPropertyDescription propertyDescriptor) {
		super();
		this.propertyModel = propertyModel;
		this.propertyDescriptor = propertyDescriptor;
		propertyAccessor = propertyDescriptor.getPropertyAccessor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IBeanProperty#getPropertyDescriptor()
	 */
	protected IPropertyDescription getPropertyDescriptor() {
		return propertyDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.object.PropertyModel#getClassDescriptor()
	 */
	@Override
	protected IClassDescription getClassDescriptor() {
		return propertyDescriptor.getPropertyTypeDescriptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.object.PropertyModel#getAnnotatedElement()
	 */
	@Override
	protected AnnotatedElement getAnnotatedElement() {
		return propertyDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IPropertyModel#getValueType()
	 */
	public Class<?> getValueType() {
		return propertyDescriptor.getPropertyType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IPropertyModel#getElementTypes()
	 */
	public Class<?>[] getElementTypes() {
		return propertyDescriptor.getElementTypes();
	}

	protected Method getEventListenerMethod(Object eventSource,
			String methodName, Class<?>... parameterTypes)
			throws NoSuchMethodException {
		Class<?> sourceClass = eventSource.getClass();
		return sourceClass.getMethod(methodName, parameterTypes);
	}

	protected void handleCannotAddEventListener(Object eventSource,
			Throwable exception) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.object.PropertyModel#firstPropertyChangeListenerAdded
	 * ()
	 */
	@Override
	protected void addPropertyChangeListenerTo(Object eventSource) {
		super.addPropertyChangeListenerTo(eventSource);
		if (eventSource != null) {
			try {
				Method addEventListenerMethod = getEventListenerMethod(
						eventSource, "addPropertyChangeListener", String.class, //$NON-NLS-1$
						PropertyChangeListener.class);
				addEventListenerMethod.invoke(eventSource, propertyDescriptor
						.getPropertyName(), this);
			} catch (InvocationTargetException ex) {
				handleCannotAddEventListener(eventSource, ex.getCause());
			} catch (Exception ex) {
				handleCannotAddEventListener(eventSource, ex);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		firePropertyChangeEvent(propertyChangeEvent.getOldValue(),
				propertyChangeEvent.getNewValue());
	}

	protected void handleCannotRemoveEventListener(Object eventSource,
			Throwable exception) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.object.PropertyModel#lastPropertyChangeListenerRemoved
	 * ()
	 */
	@Override
	protected void removePropertyChangeListenerFrom(Object eventSource) {
		if (eventSource != null) {
			try {
				Method removeEventListenerMethod = getEventListenerMethod(
						eventSource,
						"removePropertyChangeListener", String.class, //$NON-NLS-1$
						PropertyChangeListener.class);
				removeEventListenerMethod.invoke(eventSource,
						propertyDescriptor.getPropertyName(), this);
			} catch (InvocationTargetException ex) {
				handleCannotRemoveEventListener(eventSource, ex.getCause());
			} catch (Exception ex) {
				handleCannotRemoveEventListener(eventSource, ex);
			}
		}
		super.removePropertyChangeListenerFrom(eventSource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IBeanProperty#isReadOnly()
	 */
	public boolean isReadOnly() {
		Object baseValue = propertyModel.getValue();
		if (baseValue != null) {
			return propertyAccessor.isReadOnlyFor(baseValue);
		}
		return true;
	}

	protected Object getValueFrom(Object sourceValue) {
		if (sourceValue != null) {
			return propertyAccessor.getValueFrom(sourceValue);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IBeanProperty#getValue()
	 */
	public Object getValue() {
		return getValueFrom(propertyModel.getValue());
	}

	protected void setValueInto(Object value, Object sourceValue) {
		if (sourceValue != null) {
			propertyAccessor.setValueInto(value, sourceValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IBeanProperty#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		setValueInto(value, propertyModel.getValue());
	}

	@Override
	protected void valueChanged(Object oldValue, Object newValue) {
		removePropertyChangeListenerFrom(oldValue);
		addPropertyChangeListenerTo(newValue);
		oldValue = getValueFrom(oldValue);
		newValue = getValueFrom(newValue);
		firePropertyChangeEvent(oldValue, newValue);
		super.valueChanged(oldValue, newValue);
	}

}
