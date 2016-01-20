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
package org.jmetis.observable.decorator;

import java.beans.PropertyChangeListener;
import java.lang.annotation.Annotation;

import org.jmetis.observable.IPropertyModel;

/**
 * {@code PropertyModelDecorator} allows new/additional behavior to be added to
 * an {@link IPropertyModel} dynamically.
 * 
 * @author aerlach
 */
public class PropertyModelDecorator implements IPropertyModel {

	private IPropertyModel decoratedPropertyModel;

	/**
	 * Constructs a new {@code PropertyModelDecorator} instance.
	 */
	public PropertyModelDecorator(IPropertyModel propertyModel) {
		super();
		this.decoratedPropertyModel = propertyModel;
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
		this.decoratedPropertyModel
				.addPropertyChangeListener(propertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IPropertyModel#getElementTypes()
	 */
	public Class<?>[] getElementTypes() {
		return this.decoratedPropertyModel.getElementTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IPropertyModel#getPropertyNamed(java.lang.String)
	 */
	public IPropertyModel getPropertyNamed(String propertyName) {
		return this.decoratedPropertyModel.getPropertyNamed(propertyName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IPropertyModel#getValue()
	 */
	public Object getValue() {
		return this.decoratedPropertyModel.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IPropertyModel#getValueType()
	 */
	public Class<?> getValueType() {
		return this.decoratedPropertyModel.getValueType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IPropertyModel#isReadOnly()
	 */
	public boolean isReadOnly() {
		return this.decoratedPropertyModel.isReadOnly();
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
		this.decoratedPropertyModel
				.removePropertyChangeListener(propertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.observable.IPropertyModel#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		this.decoratedPropertyModel.setValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getAnnotation(java.lang.Class)
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return this.decoratedPropertyModel.getAnnotation(annotationClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getAnnotations()
	 */
	public Annotation[] getAnnotations() {
		return this.decoratedPropertyModel.getAnnotations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getDeclaredAnnotations()
	 */
	public Annotation[] getDeclaredAnnotations() {
		return this.decoratedPropertyModel.getDeclaredAnnotations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.lang.reflect.AnnotatedElement#isAnnotationPresent(java.lang.Class)
	 */
	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		return this.decoratedPropertyModel.isAnnotationPresent(annotationClass);
	}

}
