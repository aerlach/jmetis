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
package org.jmetis.reflection.metadata;

import java.lang.annotation.Annotation;

import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code ClassDescriptorDecorator}
 * 
 * @author aerlach
 */
public class ClassDescriptorDecorator implements IClassDescription {

	private IClassDescription decoratedClassDescriptor;

	/**
	 * Constructs a new {@code ClassDescriptorDecorator} instance.
	 */
	public ClassDescriptorDecorator() {
		super();
	}

	public ClassDescriptorDecorator(IClassDescription decoratedClassDescriptor) {
		this();
		this.decoratedClassDescriptor = decoratedClassDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IClassDescriptor#allPropertyDescriptors()
	 */
	public IPropertyDescription[] allPropertyDescriptors() {
		if (decoratedClassDescriptor == null) {
			return IClassDescription.EMPTY_PROPERTY_DESCRIPTOR_ARRAY;
		}
		return decoratedClassDescriptor.allPropertyDescriptors();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IClassDescriptor#allPropertyNames()
	 */
	public String[] allPropertyNames() {
		if (decoratedClassDescriptor == null) {
			return IClassDescription.EMPTY_PROPERTY_NAME_ARRAY;
		}
		return decoratedClassDescriptor.allPropertyNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IClassDescriptor#getDescribedClass()
	 */
	public Class<?> getDescribedClass() {
		if (decoratedClassDescriptor == null) {
			return Object.class;
		}
		return decoratedClassDescriptor.getDescribedClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IClassDescriptor#getPropertyDescriptorNamed
	 * (java.lang.String)
	 */
	public IPropertyDescription getPropertyDescriptorNamed(String propertyName) {
		if (decoratedClassDescriptor == null) {
			return null;
		}
		return decoratedClassDescriptor
				.getPropertyDescriptorNamed(propertyName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IClassDescriptor#getSuperclassDescriptor()
	 */
	public IClassDescription getSuperclassDescriptor() {
		if (decoratedClassDescriptor == null) {
			return null;
		}
		return decoratedClassDescriptor.getSuperclassDescriptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IClassDescriptor#hasPropertyNamed(java.lang
	 * .String)
	 */
	public boolean hasPropertyNamed(String propertyName) {
		if (decoratedClassDescriptor == null) {
			return false;
		}
		return decoratedClassDescriptor.hasPropertyNamed(propertyName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IClassDescriptor#hasSuperclassDescriptor()
	 */
	public boolean hasSuperclassDescriptor() {
		if (decoratedClassDescriptor == null) {
			return false;
		}
		return decoratedClassDescriptor.hasSuperclassDescriptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getAnnotation(java.lang.Class)
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		if (decoratedClassDescriptor == null) {
			return null;
		}
		return decoratedClassDescriptor.getAnnotation(annotationClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getAnnotations()
	 */
	public Annotation[] getAnnotations() {
		if (decoratedClassDescriptor == null) {
			return IClassDescription.EMPTY_ANNOTATION_ARRAY;
		}
		return decoratedClassDescriptor.getAnnotations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getDeclaredAnnotations()
	 */
	public Annotation[] getDeclaredAnnotations() {
		if (decoratedClassDescriptor == null) {
			return IClassDescription.EMPTY_ANNOTATION_ARRAY;
		}
		return decoratedClassDescriptor.getDeclaredAnnotations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.lang.reflect.AnnotatedElement#isAnnotationPresent(java.lang.Class)
	 */
	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		if (decoratedClassDescriptor == null) {
			return false;
		}
		return decoratedClassDescriptor.isAnnotationPresent(annotationClass);
	}

}
