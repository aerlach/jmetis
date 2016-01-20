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
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.kernel.metadata.IPropertyDescription;
import org.jmetis.observable.IPropertyModel;

/**
 * {@code PropertyModel}
 * 
 * @author aerlach
 */
public abstract class PropertyModel implements IPropertyModel {

	protected static final Class<?>[] EMPTY_CLASS_ARRAY = {};

	private Map<String, PropertyAdapter> propertyAdapters;

	private PropertyChangeListener[] propertyChangeListeners;

	/**
	 * Constructs a new {@code PropertyModel} instance.
	 */
	protected PropertyModel() {
		super();
	}

	protected abstract AnnotatedElement getAnnotatedElement();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.lang.reflect.AnnotatedElement#isAnnotationPresent(java.lang.Class)
	 */
	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		return getAnnotatedElement().isAnnotationPresent(annotationClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getAnnotation(java.lang.Class)
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return getAnnotatedElement().getAnnotation(annotationClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getAnnotations()
	 */
	public Annotation[] getAnnotations() {
		return getAnnotatedElement().getAnnotations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getDeclaredAnnotations()
	 */
	public Annotation[] getDeclaredAnnotations() {
		return getAnnotatedElement().getDeclaredAnnotations();
	}

	protected abstract IClassDescription getClassDescriptor();

	protected PropertyAdapter createPropertyAdapterFor(
			IPropertyDescription propertyDescriptor) {
		return new PropertyAdapter(this, propertyDescriptor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IPropertyModel#getPropertyNamed(java.lang.String)
	 */
	public final IPropertyModel getPropertyNamed(String propertyName) {
		PropertyAdapter propertyAdapter;
		if (propertyAdapters != null) {
			propertyAdapter = propertyAdapters.get(propertyName);
			if (propertyAdapter != null) {
				return propertyAdapter;
			}
		} else {
			propertyAdapters = new HashMap<String, PropertyAdapter>();
		}
		propertyAdapter = createPropertyAdapterFor(Assertions.mustNotBeNull(
				"propertyDescriptor", getClassDescriptor()
						.getPropertyDescriptorNamed(propertyName)));
		propertyAdapters.put(propertyName, propertyAdapter);
		return propertyAdapter;
	}

	/**
	 * Registers the {@code PropertyModel} with the given {@code eventSource}.
	 * 
	 * @param eventSource
	 */
	protected void addPropertyChangeListenerTo(Object eventSource) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IPropertyModel#addPropertyChangeListener(java.beans
	 * .PropertyChangeListener)
	 */
	public void addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListener != null) {
			if (propertyChangeListeners == null) {
				propertyChangeListeners = new PropertyChangeListener[] { propertyChangeListener };
				addPropertyChangeListenerTo(getValue());
			} else {
				for (int i = 0, n = propertyChangeListeners.length; i < n; ++i) {
					if (propertyChangeListeners[i]
							.equals(propertyChangeListener)) {
						return;
					}
				}
				PropertyChangeListener[] propertyChangeListeners = new PropertyChangeListener[this.propertyChangeListeners.length + 1];
				System.arraycopy(this.propertyChangeListeners, 0,
						propertyChangeListeners, 0,
						this.propertyChangeListeners.length);
				propertyChangeListeners[this.propertyChangeListeners.length] = propertyChangeListener;
				this.propertyChangeListeners = propertyChangeListeners;
			}
		}
	}

	protected boolean hasPropertyChangeListeners() {
		return propertyChangeListeners != null;
	}

	protected PropertyChangeEvent createPropertyChangeEvent(Object oldValue,
			Object newValue) {
		return new PropertyChangeEvent(this, "value", oldValue, newValue);
	}

	protected void firePropertyChangeEvent(Object oldValue, Object newValue) {
		if (propertyChangeListeners != null) {
			PropertyChangeEvent propertyChangeEvent = createPropertyChangeEvent(
					oldValue, newValue);
			for (PropertyChangeListener propertyChangeListener : propertyChangeListeners) {
				propertyChangeListener.propertyChange(propertyChangeEvent);
			}

		}
	}

	/**
	 * Unregisters the {@code PropertyModel} from the given {@code eventSource}.
	 * 
	 * @param eventSoruce
	 */
	protected void removePropertyChangeListenerFrom(Object eventSource) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IPropertyModel#removePropertyChangeListener(java
	 * .beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListener != null) {
			if (propertyChangeListeners.length == 1) {
				if (propertyChangeListeners[0].equals(propertyChangeListener)) {
					propertyChangeListeners = null;
					removePropertyChangeListenerFrom(getValue());
				}
			} else {
				for (int i = 0, n = propertyChangeListeners.length; i < n; ++i) {
					if (propertyChangeListeners[i]
							.equals(propertyChangeListener)) {
						PropertyChangeListener[] propertyChangeListeners = new PropertyChangeListener[this.propertyChangeListeners.length - 1];
						System.arraycopy(this.propertyChangeListeners, 0,
								propertyChangeListeners, 0, i);
						System.arraycopy(this.propertyChangeListeners, i + 1,
								propertyChangeListeners, i,
								this.propertyChangeListeners.length - i - 1);
						this.propertyChangeListeners = propertyChangeListeners;
						return;
					}
				}
			}
		}
	}

	protected void valueChanged(Object oldValue, Object newValue) {
		if (propertyAdapters != null) {
			for (PropertyAdapter propertyAdapter : propertyAdapters.values()) {
				propertyAdapter.valueChanged(oldValue, newValue);
			}
		}
	}

}
