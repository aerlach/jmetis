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
package org.jmetis.collections.observable;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * {@code IIndexedModel} notifies listeners of changes.
 * 
 * @author aerlach
 */
public interface IIndexedModel<E> extends List<E> {
	/**
	 * Adds a listener that is notified when the list changes.
	 * 
	 * @param propertyChangeListener
	 *            the listener to add
	 */
	void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	/**
	 * Removes a listener.
	 * 
	 * @param propertyChangeListener
	 *            the listener to remove
	 */
	void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener);

}
