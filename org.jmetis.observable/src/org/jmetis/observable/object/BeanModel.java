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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.jmetis.observable.IBeanModel;

/**
 * {@code BeanModel} is a base implementation for observable objects based on
 * {@link PropertyChangeSupport}.
 * 
 * @author aerlach
 */
public abstract class BeanModel implements IBeanModel {

	private PropertyChangeSupport propertyChangeListeners;

	/**
	 * Constructs a new {@code BeanModel} instance.
	 */
	protected BeanModel() {
		super();
	}

	protected synchronized PropertyChangeSupport propertyChangeListeners() {
		if (propertyChangeListeners == null) {
			propertyChangeListeners = new PropertyChangeSupport(this);
		}
		return propertyChangeListeners;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IBeanModel#addPropertyChangeListener(java.beans.
	 * PropertyChangeListener)
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
	 * @see
	 * org.jmetis.observable.IBeanModel#addPropertyChangeListener(java.lang.
	 * String , java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListeners == null) {
			propertyChangeListeners = new PropertyChangeSupport(this);
		}
		propertyChangeListeners.addPropertyChangeListener(propertyName,
				propertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IBeanModel#removePropertyChangeListener(java.beans
	 * .PropertyChangeListener)
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
	 * org.jmetis.observable.IBeanModel#removePropertyChangeListener(java.lang
	 * .String, java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListeners != null) {
			propertyChangeListeners.removePropertyChangeListener(propertyName,
					propertyChangeListener);
		}
	}

	protected void firePropertyChangeEvent(String propertyName,
			Object oldValue, Object newValue) {
		if (propertyChangeListeners != null) {
			propertyChangeListeners.firePropertyChange(propertyName, oldValue,
					newValue);
		}
	}

	protected void firePropertyChangeEvent(String propertyName,
			boolean oldValue, boolean newValue) {
		if (propertyChangeListeners != null && oldValue != newValue) {
			propertyChangeListeners.firePropertyChange(propertyName, oldValue,
					newValue);
		}
	}

	protected void firePropertyChangeEvent(String propertyName,
			double oldValue, double newValue) {
		if (propertyChangeListeners != null && oldValue != newValue) {
			propertyChangeListeners.firePropertyChange(propertyName, oldValue,
					newValue);
		}
	}

	protected void firePropertyChangeEvent(String propertyName, float oldValue,
			float newValue) {
		if (propertyChangeListeners != null && oldValue != newValue) {
			propertyChangeListeners.firePropertyChange(propertyName, oldValue,
					newValue);
		}
	}

	protected void firePropertyChangeEvent(String propertyName, int oldValue,
			int newValue) {
		if (propertyChangeListeners != null && oldValue != newValue) {
			propertyChangeListeners.firePropertyChange(propertyName, oldValue,
					newValue);
		}
	}

	protected void firePropertyChangeEvent(String propertyName, long oldValue,
			long newValue) {
		if (propertyChangeListeners != null && oldValue != newValue) {
			propertyChangeListeners.firePropertyChange(propertyName, oldValue,
					newValue);
		}
	}

}
