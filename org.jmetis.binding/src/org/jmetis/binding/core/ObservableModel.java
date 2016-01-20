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
package org.jmetis.binding.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jmetis.binding.BindingException;
import org.jmetis.binding.IObservableProperty;
import org.jmetis.kernel.metadata.IPropertyAccessor;
import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code ObservableModel} holds a generic value. If the value changes, a
 * {@link PropertyChangeEvent} is fired that can be observed using a
 * {@link PropertyChangeListener}.
 * 
 * @author aerlach
 */
public abstract class ObservableModel implements IObservableProperty {

	protected IPropertyDescription propertyDescriptor;

	protected PropertyChangeSupport propertyChangeListeners;

	protected PropertyChangeListener valueChangeHandler;

	/**
	 * Constructs a new {@code ObservableModel} instance.
	 */
	protected ObservableModel(IPropertyDescription propertyDescriptor) {
		super();
		this.propertyDescriptor = propertyDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#isConstantProperty()
	 */
	public boolean isConstantProperty() {
		return false;
	}

	public IPropertyDescription getPropertyDescriptor() {
		return propertyDescriptor;
	}

	protected IPropertyAccessor getPropertyAccessor() {
		return propertyDescriptor.getPropertyAccessor();
	}

	protected boolean hasPropertyChangeListeners() {
		return propertyChangeListeners != null
				&& propertyChangeListeners.hasListeners(propertyDescriptor
						.getPropertyName());
	}

	protected PropertyDescriptor handlePropertyNotFound(
			Class<?> declaringClass, String propertyName) {
		BindingException.propertyNotFound(declaringClass, propertyName);
		return null;
	}

	protected Object handleCannotReadProperty(Object observedValue) {
		BindingException.cannotReadProperty(observedValue, propertyDescriptor);
		return null;
	}

	protected Object handleReadPropertyFailed(Object observedValue,
			Throwable exception) {
		BindingException.readPropertyFailed(observedValue, propertyDescriptor,
				exception);
		return null;
	}

	protected Object getValueFrom(Object sourceValue) {
		if (sourceValue != null) {
			return getPropertyAccessor().getValueFrom(sourceValue);
		}
		return null;
	}

	protected Method getListenerMethod(Object eventSource, String methodName,
			Class<?>... parameterTypes) throws NoSuchMethodException {
		Class<?> sourceClass = eventSource.getClass();
		return sourceClass.getMethod(methodName, parameterTypes);
	}

	protected void handleCannotAddPropertyChangeListener(Object eventSource,
			Throwable exception) {
		BindingException.cannotAddPropertyChangeListener(eventSource,
				propertyDescriptor, exception);
	}

	protected void addPropertyChangeListenerTo(String propertyName,
			PropertyChangeListener propertyChangeListener, Object eventSource) {
		if (eventSource != null) {
			try {
				Method addPropertyChangeListenerMethod = getListenerMethod(
						eventSource, "addPropertyChangeListener", String.class, //$NON-NLS-1$
						PropertyChangeListener.class);
				addPropertyChangeListenerMethod.invoke(eventSource,
						propertyName, propertyChangeListener);
			} catch (InvocationTargetException ex) {
				handleCannotAddPropertyChangeListener(eventSource, ex
						.getCause());
			} catch (Exception ex) {
				handleCannotAddPropertyChangeListener(eventSource, ex);
			}
		}
	}

	protected void addPropertyChangeListenerTo(
			PropertyChangeListener propertyChangeListener, Object eventSource) {
		this.addPropertyChangeListenerTo(propertyDescriptor.getPropertyName(),
				propertyChangeListener, eventSource);
	}

	protected void addPropertyChangeListenerTo(Object eventSource) {
		this.addPropertyChangeListenerTo(valueChangeHandler, eventSource);
	}

	protected void handleCannotRemovePropertyChangeListener(Object eventSource,
			Throwable exception) {
		BindingException.cannotRemovePropertyChangeListener(eventSource,
				propertyDescriptor, exception);
	}

	protected void removePropertyChangeListenerFrom(String propertyName,
			PropertyChangeListener propertyChangeListener, Object eventSource) {
		if (eventSource != null) {
			try {
				Method addPropertyChangeListenerMethod = getListenerMethod(
						eventSource,
						"removePropertyChangeListener", String.class, //$NON-NLS-1$
						PropertyChangeListener.class);
				addPropertyChangeListenerMethod.invoke(eventSource,
						propertyName, propertyChangeListener);
			} catch (InvocationTargetException ex) {
				handleCannotRemovePropertyChangeListener(eventSource, ex
						.getCause());
			} catch (Exception ex) {
				handleCannotRemovePropertyChangeListener(eventSource, ex);
			}
		}
	}

	protected void removePropertyChangeListenerFrom(
			PropertyChangeListener propertyChangeListener, Object eventSource) {
		this.removePropertyChangeListenerFrom(propertyDescriptor
				.getPropertyName(), propertyChangeListener, eventSource);
	}

	protected void removePropertyChangeListenerFrom(Object eventSource) {
		this.removePropertyChangeListenerFrom(valueChangeHandler, eventSource);
	}

	protected abstract void forwardValueChanged(
			PropertyChangeEvent propertyChangeEvent);

	protected void firstPropertyChangeListenerAdded() {
		valueChangeHandler = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
				ObservableModel.this.forwardValueChanged(propertyChangeEvent);
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#addValueChangeListener(java
	 * .beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListeners == null) {
			propertyChangeListeners = new PropertyChangeSupport(this);
		}
		boolean hadPropertyChangeListeners = hasPropertyChangeListeners();
		propertyChangeListeners
				.addPropertyChangeListener(propertyChangeListener);
		if (!hadPropertyChangeListeners) {
			firstPropertyChangeListenerAdded();
		}
	}

	protected void lastPropertyChangeListenerRemoved() {
		// Intentionally empty block
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#removeValueChangeListener(
	 * java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListeners != null) {
			propertyChangeListeners
					.removePropertyChangeListener(propertyChangeListener);
			if (!hasPropertyChangeListeners()) {
				lastPropertyChangeListenerRemoved();
			}
		}
	}

	protected void valueChanged(Object oldValue, Object newValue) {
		if (hasPropertyChangeListeners()) {
			propertyChangeListeners.firePropertyChange(propertyDescriptor
					.getPropertyName(), oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#getValueType()
	 */
	public Object getValueType() {
		return propertyDescriptor.getPropertyType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.binding.IObservableProperty#getAnnotation(java.lang.Class)
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return propertyDescriptor.getAnnotation(annotationClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#getAnnotations()
	 */
	public Annotation[] getAnnotations() {
		return propertyDescriptor.getAnnotations();
	}

	protected abstract Object getValueHolder();

	public Object getValue() {
		return getValueFrom(getValueHolder());
	}

	protected void handleCannotWriteProperty(Object value, Object observedValue) {
		BindingException.cannotWriteProperty(observedValue, value,
				propertyDescriptor);
	}

	protected void handleWritePropertyFailed(Object observedValue,
			Object value, Throwable exception) {
		BindingException.writePropertyFailed(observedValue, value,
				propertyDescriptor, exception);
	}

	protected void setValueInto(Object value, Object sourceValue) {
		if (sourceValue != null) {
			getPropertyAccessor().setValueInto(value, sourceValue);
		}
	}

	public void setValue(Object value) {
		setValueInto(value, getValueHolder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return propertyDescriptor.getPropertyName();
	}

}
