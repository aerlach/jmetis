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

import java.beans.PropertyChangeListener;

/**
 * {@code IPropertyChangeDispatcher}
 * 
 * @author aerlach
 */
public interface IPropertyChangeDispatcher {

	/**
	 * @param propertyName
	 * @param propertyChangeListener
	 */
	IPropertyChangeDispatcher addPropertyChangeListener(String propertyName,
			PropertyChangeListener propertyChangeListener);

	/**
	 * @param propertyChangeListener
	 */
	IPropertyChangeDispatcher addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener);

	/**
	 * @param propertyName
	 * @param propertyChangeListener
	 */
	IPropertyChangeDispatcher removePropertyChangeListener(String propertyName,
			PropertyChangeListener propertyChangeListener);

	/**
	 * @param propertyChangeListener
	 */
	IPropertyChangeDispatcher removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener);

	/**
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	void firePropertyChangeEvent(String propertyName, Object oldValue,
			Object newValue);

	/**
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	void firePropertyChangeEvent(String propertyName, boolean oldValue,
			boolean newValue);

	/**
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	void firePropertyChangeEvent(String propertyName, byte oldValue,
			byte newValue);

	/**
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	void firePropertyChangeEvent(String propertyName, char oldValue,
			char newValue);

	/**
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	void firePropertyChangeEvent(String propertyName, double oldValue,
			double newValue);

	/**
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	void firePropertyChangeEvent(String propertyName, float oldValue,
			float newValue);

	/**
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	void firePropertyChangeEvent(String propertyName, int oldValue, int newValue);

	/**
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	void firePropertyChangeEvent(String propertyName, long oldValue,
			long newValue);

	/**
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	void firePropertyChangeEvent(String propertyName, short oldValue,
			short newValue);

}
