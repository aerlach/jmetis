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
package org.jmetis.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.annotation.Annotation;

import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code IObservableProperty} represents a model with a generic access to a
 * single value that allow to observe value changes. The value can be accessed
 * using the {@code #getValue()}, and {@code #setValue(Object)} methods.
 * Observers can register instances of {@link PropertyChangeListener} to be
 * notified if the value changes.
 * 
 * @author aerlach
 */
public interface IObservableProperty {

	boolean isConstantProperty();

	/**
	 * Registers the given {@link PropertyChangeListener} with this {@code
	 * IObservableProperty}. The {@code propertyChangeListener} will be notified
	 * if the value has changed. The {@link PropertyChangeEvent}s delivered to
	 * the observers must have the name set to "value".
	 * 
	 * @param propertyChangeListener
	 *            the listener to be added
	 */
	void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	/**
	 * Unregisters the given {@link PropertyChangeListener} from this {@code
	 * IObservableProperty}.
	 * 
	 * @param propertyChangeListener
	 *            the listener to be removed
	 */
	void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener);

	/**
	 * Returns the value type of this observable model, or {@code null} if this
	 * observable value is untyped.
	 * 
	 * @return the value type, or {@code null}
	 */
	Object getValueType();

	/**
	 * Returns the annotation for the given type of this observable model if
	 * such an annotation is present, else {@code null}.
	 * 
	 * @param annotationClass
	 *            the Class object corresponding to the annotation type
	 * @return the annotation for the given annotation type of this observable
	 *         model if present on this element, else {@code null}
	 * @throws NullPointerException
	 *             if the given annotation class is null
	 */
	<T extends Annotation> T getAnnotation(Class<T> annotationClass);

	/**
	 * Returns all annotations present on this element. (Returns an array of
	 * length zero if this element has no annotations.) The caller of this
	 * method is free to modify the returned array; it will have no effect on
	 * the arrays returned to other callers.
	 * 
	 * @return all annotations present on this element
	 */
	Annotation[] getAnnotations();

	/**
	 * 
	 * @return
	 */
	IPropertyDescription getPropertyDescriptor();

	/**
	 * Returns the value of this observable model.
	 * 
	 * @return the value of this observable model
	 */
	Object getValue();

	/**
	 * Sets a new value and notifies any registered observers if the value has
	 * changed. In case of a read-only value implementors may choose to either
	 * reject this operation or to do nothing.
	 * 
	 * @param value
	 *            the value to be set
	 */
	void setValue(Object value);

}
