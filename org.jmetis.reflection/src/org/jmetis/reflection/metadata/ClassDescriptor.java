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
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.kernel.metadata.IMetaDataIntrospector;
import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code ClassDescriptor}
 * 
 * @author aerlach
 */
public class ClassDescriptor implements IClassDescription {

	private IMetaDataIntrospector metaDataIntrospector;

	private Class<?> describedClass;

	private Map<TypeVariable<?>, Type> typeVariableMap;

	private IClassDescription superclassDescriptor;

	private Map<String, PropertyDescriptor> propertyDescriptors;

	public ClassDescriptor(IMetaDataIntrospector metaDataIntrospector,
			Class<?> describedClass) {
		super();
		this.metaDataIntrospector = this.mustNotBeNull("metaDataIntrospector",
				metaDataIntrospector);
		this.describedClass = this.mustNotBeNull("describedClass",
				describedClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Assertions#mustNotBeNull(String, Object)
	 */
	protected <T> T mustNotBeNull(String name, T value) {
		return Assertions.mustNotBeNull(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Assertions#mustNotBeNullOrEmpty(String, Object[])
	 */
	protected String mustNotBeNullOrEmpty(String name, String value) {
		return Assertions.mustNotBeNullOrEmpty(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.reflection.metadata.AnnotatedDescriptor#metaDataIntrospector()
	 */
	protected IMetaDataIntrospector metaDataIntrospector() {
		return metaDataIntrospector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IClassDescriptor#describedClass()
	 */
	public Class<?> getDescribedClass() {
		return describedClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.lang.reflect.AnnotatedElement#isAnnotationPresent(java.lang.Class)
	 */
	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		return describedClass.isAnnotationPresent(annotationClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getAnnotation(java.lang.Class)
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return describedClass.getAnnotation(annotationClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getDeclaredAnnotations()
	 */
	public Annotation[] getDeclaredAnnotations() {
		return describedClass.getDeclaredAnnotations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getAnnotations()
	 */
	public Annotation[] getAnnotations() {
		return describedClass.getAnnotations();
	}

	protected Map<TypeVariable<?>, Type> typeVariableMap() {
		if (typeVariableMap == null) {
			typeVariableMap = metaDataIntrospector
					.typeVariableMapOf(describedClass);
		}
		return typeVariableMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IGenericDescriptor#typeArgumentDescriptors()
	 */
	public IClassDescription[] typeArgumentDescriptors() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IClassDescriptor#hasSuperclassDescriptor()
	 */
	public boolean hasSuperclassDescriptor() {
		return describedClass.getSuperclass() != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IClassDescriptor#superclassDescriptor()
	 */
	public IClassDescription getSuperclassDescriptor() {
		if (superclassDescriptor == null) {
			Class<?> superclass = describedClass.getSuperclass();
			if (superclass != null) {
				superclassDescriptor = metaDataIntrospector()
						.classDescriptorOf(superclass);
			}
		}
		return superclassDescriptor;
	}

	protected Map<String, PropertyDescriptor> createPropertyDescriptors() {
		Map<String, Method[]> propertyMethods = metaDataIntrospector()
				.propertyMethodsOf(describedClass);
		Map<String, PropertyDescriptor> propertyDescriptors = new HashMap<String, PropertyDescriptor>(
				propertyMethods.size() * 2);
		for (Map.Entry<String, Method[]> entry : propertyMethods.entrySet()) {
			propertyDescriptors.put(entry.getKey(), new PropertyDescriptor(
					this, entry.getKey(), entry.getValue()[0],
					entry.getValue()[1]));
		}
		return propertyDescriptors;
	}

	protected Map<String, PropertyDescriptor> propertyDescriptors() {
		if (propertyDescriptors == null) {
			propertyDescriptors = createPropertyDescriptors();
		}
		return propertyDescriptors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.metadata.IClassDescriptor#propertyNames()
	 */
	public String[] allPropertyNames() {
		Map<String, PropertyDescriptor> propertyDescriptors = propertyDescriptors();
		if (propertyDescriptors.isEmpty()) {
			if (hasSuperclassDescriptor()) {
				return getSuperclassDescriptor().allPropertyNames();
			}
			return IClassDescription.EMPTY_PROPERTY_NAME_ARRAY;
		}
		if (hasSuperclassDescriptor()) {
			String[] inheritedPropertyNames = getSuperclassDescriptor()
					.allPropertyNames();
			String[] ownPropertyNames = propertyDescriptors.keySet().toArray(
					new String[propertyDescriptors.size()]);
			String[] allPropertyNames = new String[inheritedPropertyNames.length
					+ ownPropertyNames.length];
			System.arraycopy(ownPropertyNames, 0, allPropertyNames, 0,
					ownPropertyNames.length);
			System.arraycopy(inheritedPropertyNames, 0, allPropertyNames,
					ownPropertyNames.length, inheritedPropertyNames.length);
			return allPropertyNames;
		}
		return propertyDescriptors.keySet().toArray(
				new String[propertyDescriptors.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IClassDescriptor#allPropertyDescriptors()
	 */
	public IPropertyDescription[] allPropertyDescriptors() {
		Map<String, PropertyDescriptor> propertyDescriptors = propertyDescriptors();
		if (propertyDescriptors.isEmpty()) {
			if (hasSuperclassDescriptor()) {
				return getSuperclassDescriptor().allPropertyDescriptors();
			}
			return IClassDescription.EMPTY_PROPERTY_DESCRIPTOR_ARRAY;
		}
		if (hasSuperclassDescriptor()) {
			IPropertyDescription[] inheritedPropertyDescriptors = getSuperclassDescriptor()
					.allPropertyDescriptors();
			IPropertyDescription[] ownPropertyDescriptors = propertyDescriptors
					.values()
					.toArray(
							new IPropertyDescription[propertyDescriptors.size()]);
			IPropertyDescription[] allPropertyDescriptors = new IPropertyDescription[inheritedPropertyDescriptors.length
					+ ownPropertyDescriptors.length];
			System.arraycopy(ownPropertyDescriptors, 0, allPropertyDescriptors,
					0, ownPropertyDescriptors.length);
			System.arraycopy(inheritedPropertyDescriptors, 0,
					allPropertyDescriptors, ownPropertyDescriptors.length,
					inheritedPropertyDescriptors.length);
			return allPropertyDescriptors;
		}
		return propertyDescriptors.values().toArray(
				new IPropertyDescription[propertyDescriptors.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IClassDescriptor#hasPropertyNamed(java.lang
	 * .String)
	 */
	public boolean hasPropertyNamed(String propertyName) {
		return getPropertyDescriptorNamed(propertyName) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IClassDescriptor#propertyDescriptorNamed
	 * (java.lang.String)
	 */
	public IPropertyDescription getPropertyDescriptorNamed(String propertyName) {
		IPropertyDescription propertyDescriptor = propertyDescriptors().get(
				propertyName);
		if (propertyDescriptor == null && hasSuperclassDescriptor()) {
			propertyDescriptor = getSuperclassDescriptor()
					.getPropertyDescriptorNamed(propertyName);
		}
		return propertyDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (this == otherObject) {
			return true;
		}
		if (otherObject instanceof ClassDescriptor) {
			return describedClass == ((ClassDescriptor) otherObject).describedClass;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return describedClass.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.reflection.metadata.AnnotatedDescriptor#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getClass().getSimpleName()).append(
				"(\n\tdescribedClass: ").append(describedClass.getName())
				.append("\n\tannotations: [");
		Annotation[] annotations = getAnnotations();
		if (annotations.length > 0) {
			stringBuilder.append(annotations[0].annotationType()
					.getSimpleName());
			for (int i = 1, n = annotations.length; i < n; i++) {
				stringBuilder.append(", ").append(
						annotations[i].annotationType().getSimpleName());
			}
		}
		stringBuilder.append(']').append("\n\tpropertyNames: [");
		String[] propertyNames = allPropertyNames();
		if (propertyNames.length > 0) {
			stringBuilder.append(propertyNames[0]);
			for (int i = 1, n = propertyNames.length; i < n; i++) {
				stringBuilder.append(", ").append(propertyNames[i]);
			}
		}
		// TODO
		stringBuilder.append("\n").append(typeVariableMap()).append("\n");
		// TODO
		return stringBuilder.append("]\n)").toString();
	}

}
