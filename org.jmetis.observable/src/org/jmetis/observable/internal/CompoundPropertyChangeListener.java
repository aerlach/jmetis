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
package org.jmetis.observable.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * {@code CompoundPropertyChangeListener}
 * 
 * @author aerlach
 */
public class CompoundPropertyChangeListener implements IPropertyChangeListener {

	private PropertyChangeListener[] propertyChangeListeners;

	/**
	 * Constructs a new {@code CompoundPropertyChangeListener} instance.
	 */
	public CompoundPropertyChangeListener(
			PropertyChangeListener... propertyChangeListeners) {
		super();
		this.propertyChangeListeners = propertyChangeListeners;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeListener#
	 * addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeListener addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		PropertyChangeListener[] properChangeListeners = new PropertyChangeListener[propertyChangeListeners.length + 1];
		System.arraycopy(propertyChangeListeners, 0, properChangeListeners, 0,
				propertyChangeListeners.length);
		properChangeListeners[propertyChangeListeners.length] = propertyChangeListener;
		propertyChangeListeners = properChangeListeners;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeListener#
	 * removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeListener removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		for (int i = 0, n = propertyChangeListeners.length; i < n; i++) {
			if (propertyChangeListener.equals(propertyChangeListeners[i])) {
				PropertyChangeListener[] properChangeListeners = new PropertyChangeListener[propertyChangeListeners.length - 1];
				System.arraycopy(propertyChangeListeners, 0,
						properChangeListeners, 0, i);
				System.arraycopy(propertyChangeListeners, i + 1,
						properChangeListeners, i, properChangeListeners.length
								- i);
				propertyChangeListeners = properChangeListeners;
				break;
			}
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.internal.IPropertyChangeListener#propertyChangeEvent
	 * (java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		for (PropertyChangeListener propertyChangeListener : propertyChangeListeners) {
			propertyChangeListener.propertyChange(propertyChangeEvent);
		}
	}

}
