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
package org.jmetis.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.kernel.metadata.IPropertyAccessor;
import org.jmetis.kernel.metadata.IPropertyDescription;


/**
 * {@code DummyPropertyDescriptor}
 * 
 * @author aerlach
 */
public class DummyPropertyDescriptor implements IPropertyDescription {

	/**
	 * Constructs a new {@code DummyPropertyDescriptor} instance.
	 */
	protected DummyPropertyDescriptor() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IPropertyDescriptor#classDescriptor()
	 */
	public IClassDescription getClassDescriptor() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IPropertyDescriptor#propertyAccessor()
	 */
	public IPropertyAccessor getPropertyAccessor() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IPropertyDescriptor#propertyName()
	 */
	public String getPropertyName() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IPropertyDescriptor#propertyType()
	 */
	public Class<?> getPropertyType() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IGenericDescriptor#genericType()
	 */
	public Type genericType() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IGenericDescriptor#typeArgumentDescriptors()
	 */
	public IClassDescription[] typeArgumentDescriptors() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IGenericDescriptor#typeArguments()
	 */
	public Type[] typeArguments() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IAnnotatedDescriptor#getAnnotation(java.
	 * lang.Class)
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IAnnotatedDescriptor#getAnnotations()
	 */
	public Annotation[] getAnnotations() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IAnnotatedDescriptor#isAnnotationPresent
	 * (java.lang.Class)
	 */
	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getDeclaredAnnotations()
	 */
	public Annotation[] getDeclaredAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	public Class<?>[] getElementTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public IClassDescription getPropertyTypeDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

}
