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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.annotation.Annotation;

import org.jmetis.binding.IObservableProperty;
import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code ObservableVariable} </p> NOTE: The observers registered with an
 * {@code IObservableProperty} using {@code #addValueChangeListener} will be
 * invoked only with {@link PropertyChangeEvents} that have the name set to
 * "value".
 * 
 * @author aerlach
 */
public class ObservableVariable implements IObservableProperty {

	public static final String EVENT_PROPERTY_NAME = "value"; //$NON-NLS-1$

	private PropertyChangeSupport propertyChangeListeners;

	private Object value;

	/**
	 * Constructs a new {@code ObservableVariable} instance.
	 */
	public ObservableVariable() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#isConstantProperty()
	 */
	public boolean isConstantProperty() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#addPropertyChangeListener(
	 * java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListeners == null) {
			propertyChangeListeners = new PropertyChangeSupport(this);
		}
		propertyChangeListeners
				.addPropertyChangeListener(propertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#removePropertyChangeListener
	 * (java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListeners != null) {
			propertyChangeListeners
					.removePropertyChangeListener(propertyChangeListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.binding.IObservableProperty#getAnnotation(java.lang.Class)
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#getAnnotations()
	 */
	public Annotation[] getAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#getValueType()
	 */
	public Object getValueType() {
		if (value != null) {
			return value.getClass();
		}
		return Object.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#getPropertyDescriptor()
	 */
	public IPropertyDescription getPropertyDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#getValue()
	 */
	public Object getValue() {
		return value;
	}

	protected void valueChanged(Object oldValue, Object newValue) {
		if (propertyChangeListeners != null) {
			propertyChangeListeners.firePropertyChange(
					ObservableVariable.EVENT_PROPERTY_NAME, oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.binding.IObservableProperty#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		if (this.value != value) {
			valueChanged(this.value, this.value = value);
		}
	}

}
