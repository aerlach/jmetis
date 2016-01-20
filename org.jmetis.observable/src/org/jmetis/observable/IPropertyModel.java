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
package org.jmetis.observable;

import java.beans.PropertyChangeListener;
import java.lang.reflect.AnnotatedElement;

/**
 * {@code IPropertyModel} represents a model with a generic access to a single
 * value that allow to observe value changes. The value can be accessed using
 * the {@code #getValue()}, and {@code #setValue(Object)} methods. Observers can
 * register instances of {@link PropertyChangeListener} to be notified if the
 * value changes.
 * 
 * @author aerlach
 */
public interface IPropertyModel extends AnnotatedElement {

	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	IPropertyModel getPropertyNamed(String propertyName);

	/**
	 * Registers the given {@link PropertyChangeListener} with this {@code
	 * IPropertyModel}. The {@code propertyChangeListener} will be notified if
	 * the property value has changed.
	 * 
	 * @param propertyChangeListener
	 *            the {@link PropertyChangeListener} to be added
	 */
	void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	/**
	 * Unregisters the given {@link PropertyChangeListener} from this {@code
	 * IPropertyModel}.
	 * 
	 * @param propertyChangeListener
	 *            the {@link PropertyChangeListener} to be removed
	 */
	void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener);

	/**
	 * 
	 * @return
	 */
	Class<?> getValueType();

	/**
	 * 
	 * @return
	 */
	Class<?>[] getElementTypes();

	/**
	 * Returns {@code true} if the value of the receiver cannot be modified,
	 * otherwise {@code false}.
	 * 
	 * @return {@code true} if the value of the receiver cannot be modified,
	 *         otherwise {@code false}
	 */
	boolean isReadOnly();

	/**
	 * Returns the value of the receiver.
	 * 
	 * @return the value of the receiver
	 */
	Object getValue();

	/**
	 * Sets a new value of the receiver. In case of a read-only value
	 * implementors may choose to either reject this operation or to do nothing.
	 * 
	 * @param value
	 *            the new value of the receiver
	 */
	void setValue(Object value);

}
