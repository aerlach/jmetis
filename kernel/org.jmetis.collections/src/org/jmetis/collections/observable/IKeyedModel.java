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
import java.util.Map;

/**
 * {@code IKeyedModel}
 * 
 * @author aerlach
 */
public interface IKeyedModel<K, V> extends Map<K, V> {

	/**
	 * Registers the given {@link PropertyChangeListener} with this {@code
	 * IBeanModel}. The {@code propertyChangeListener} will be notified if any
	 * property of this {@code IBeanModel} has changed.
	 * 
	 * @param propertyChangeListener
	 *            the property change listener to be added
	 */
	void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	/**
	 * Registers the given {@link PropertyChangeListener} with this {@code
	 * IBeanModel}. The {@code propertyChangeListener} will be notified if the
	 * property with the given {@code propertyName} of this {@code IBeanModel}
	 * has changed.
	 * 
	 * @param propertyChangeListener
	 *            the property change listener to be added
	 */
	void addPropertyChangeListener(String propertyName,
			PropertyChangeListener propertyChangeListener);

	/**
	 * Unregisters the given {@link PropertyChangeListener} from this {@code
	 * IBeanModel}.
	 * 
	 * @param propertyChangeListener
	 *            the property change listener to be removed
	 */
	void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener);

	/**
	 * Unregisters the given {@link PropertyChangeListener} from this {@code
	 * IBeanModel}.
	 * 
	 * @param propertyChangeListener
	 *            the property change listener to be removed
	 */
	void removePropertyChangeListener(String propertyName,
			PropertyChangeListener propertyChangeListener);

}
