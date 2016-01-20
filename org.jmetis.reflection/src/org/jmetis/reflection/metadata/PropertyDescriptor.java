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
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.kernel.metadata.IMetaDataIntrospector;
import org.jmetis.kernel.metadata.IPropertyAccessor;
import org.jmetis.kernel.metadata.IPropertyDescription;
import org.jmetis.reflection.Primitives;
import org.jmetis.reflection.property.MethodAccessor;

/**
 * {@code PropertyDescriptor}
 * 
 * @author aerlach
 */
public class PropertyDescriptor implements IPropertyDescription {

	protected static final Class<?>[] EMPTY_CLASS_ARRAY = {};

	private Annotation[] annotations;

	private Annotation[] declaredAnnotations;

	private ClassDescriptor classDescriptor;

	private String propertyName;

	private Class<?> propertyType;

	private Field declaredField;

	private Method readMethod;

	private Method writeMethod;

	private IPropertyAccessor propertyAccessor;

	/**
	 * Constructs a new {@code PropertyDescriptor} instance.
	 * 
	 * @param classDescriptor
	 * @param propertyName
	 */
	protected PropertyDescriptor(ClassDescriptor classDescriptor,
			String propertyName, Method readMethod, Method writeMethod) {
		super();
		this.classDescriptor = this.mustNotBeNull("classDescriptor",
				classDescriptor);
		this.propertyName = mustNotBeNullOrEmpty("propertyName", propertyName);
		this.readMethod = this.mustNotBeNull("readMethod", readMethod);
		this.writeMethod = writeMethod;
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
		return classDescriptor.metaDataIntrospector();
	}

	protected Class<?> declaringClass() {
		return classDescriptor.getDescribedClass();
	}

	protected Field declaredField() {
		if (declaredField == null) {
			try {
				declaredField = metaDataIntrospector().declaredFieldNamed(
						declaringClass(), propertyName);
			} catch (Exception ex) {
				// Intentionally do nothing
			}
		}
		return declaredField;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.lang.reflect.AnnotatedElement#isAnnotationPresent(java.lang.Class)
	 */
	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		if (!readMethod.isAnnotationPresent(annotationClass)) {
			Field declaredField = declaredField();
			if (declaredField != null
					&& declaredField.isAnnotationPresent(annotationClass)) {
				return true;
			}
			if (writeMethod != null) {
				return writeMethod.isAnnotationPresent(annotationClass);
			}
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getAnnotation(java.lang.Class)
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		T annotation = readMethod.getAnnotation(annotationClass);
		if (annotation == null) {
			Field declaredField = declaredField();
			if (declaredField != null) {
				annotation = declaredField.getAnnotation(annotationClass);
			}
			if (annotation == null && writeMethod != null) {
				annotation = writeMethod.getAnnotation(annotationClass);
			}
		}
		return annotation;
	}

	private Collection<Annotation> collectDeclaredAnnotationsInto(
			AnnotatedElement annotatedElement,
			Collection<Annotation> collectedDeclaredAnnotations) {
		if (annotatedElement != null) {
			Annotation[] declaredAnnotations = annotatedElement
					.getDeclaredAnnotations();
			if (declaredAnnotations.length > 0) {
				if (collectedDeclaredAnnotations == null) {
					collectedDeclaredAnnotations = new ArrayList<Annotation>();
				}
				for (Annotation annotation : declaredAnnotations) {
					collectedDeclaredAnnotations.add(annotation);
				}
			}
		}
		return collectedDeclaredAnnotations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getDeclaredAnnotations()
	 */
	public Annotation[] getDeclaredAnnotations() {
		if (declaredAnnotations == null) {
			Collection<Annotation> collectedDeclaredAnnotations = collectDeclaredAnnotationsInto(
					readMethod, null);
			collectedDeclaredAnnotations = collectDeclaredAnnotationsInto(
					declaredField(), collectedDeclaredAnnotations);
			collectedDeclaredAnnotations = collectDeclaredAnnotationsInto(
					writeMethod, collectedDeclaredAnnotations);
			if (collectedDeclaredAnnotations != null) {
				declaredAnnotations = collectedDeclaredAnnotations
						.toArray(new Annotation[collectedDeclaredAnnotations
								.size()]);
			} else {
				declaredAnnotations = IClassDescription.EMPTY_ANNOTATION_ARRAY;
			}
		}
		return declaredAnnotations;
	}

	private Collection<Annotation> collectAnnotationsInto(
			AnnotatedElement annotatedElement,
			Collection<Annotation> collectedAnnotations) {
		if (annotatedElement != null) {
			Annotation[] annotations = annotatedElement.getAnnotations();
			if (annotations.length > 0) {
				if (collectedAnnotations == null) {
					collectedAnnotations = new ArrayList<Annotation>();
				}
				for (Annotation annotation : annotations) {
					collectedAnnotations.add(annotation);
				}
			}
		}
		return collectedAnnotations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.AnnotatedElement#getAnnotations()
	 */
	public Annotation[] getAnnotations() {
		if (annotations == null) {
			Collection<Annotation> collectedAnnotations = collectAnnotationsInto(
					readMethod, null);
			collectedAnnotations = collectAnnotationsInto(declaredField(),
					collectedAnnotations);
			collectedAnnotations = collectAnnotationsInto(writeMethod,
					collectedAnnotations);
			if (collectedAnnotations != null) {
				annotations = collectedAnnotations
						.toArray(new Annotation[collectedAnnotations.size()]);
			} else {
				annotations = IClassDescription.EMPTY_ANNOTATION_ARRAY;
			}
		}
		return annotations;
	}

	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return the classDescriptor
	 */
	public IClassDescription getClassDescriptor() {
		return classDescriptor;
	}

	protected Map<TypeVariable<?>, Type> typeVariableMap() {
		return classDescriptor.typeVariableMap();
	}

	/**
	 * Returns the type that will be returned by the read method, or accepted as
	 * parameter type by the write method.
	 * 
	 * @return the type info for the property
	 * @see org.jmetis.kernel.metadata.IPropertyDescription#getPropertyType()
	 */
	public Class<?> getPropertyType() {
		if (propertyType == null) {
			propertyType = readMethod.getReturnType();
			if (propertyType == Object.class) {
				Type genericReturnType = readMethod.getGenericReturnType();
				if (genericReturnType instanceof TypeVariable<?>) {
					if (typeVariableMap().containsKey(genericReturnType)) {
						propertyType = (Class<?>) typeVariableMap().get(
								genericReturnType);
					}
					if (propertyType == Object.class) {
						Method bridgedMethod = Primitives
								.findBridgedMethod(readMethod);
						if (bridgedMethod != null) {
							genericReturnType = bridgedMethod
									.getGenericReturnType();
							if (genericReturnType instanceof TypeVariable<?>) {
								if (typeVariableMap().containsKey(
										genericReturnType)) {
									propertyType = (Class<?>) typeVariableMap()
											.get(genericReturnType);
								}
							} else {
								propertyType = (Class<?>) genericReturnType;
							}
						}
					}
				}
			}
		}
		return propertyType;
	}

	public IClassDescription getPropertyTypeDescriptor() {
		return classDescriptor.metaDataIntrospector().classDescriptorOf(
				getPropertyType());
	}

	public Class<?>[] getElementTypes() {
		Type genericType = readMethod.getGenericReturnType();
		Type[] typeArguments;
		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			typeArguments = parameterizedType.getActualTypeArguments();
		} else {
			return PropertyDescriptor.EMPTY_CLASS_ARRAY;
		}
		Class<?>[] elementTypes = new Class<?>[typeArguments.length];
		for (int i = 0, n = typeArguments.length; i < n; i++) {
			Type elementType = typeArguments[i];
			if (elementType instanceof TypeVariable<?>) {
				elementType = typeVariableMap().get(elementType);
			}
			if (elementType instanceof Class) {
				elementTypes[i] = (Class<?>) elementType;
			} else {
				elementTypes[i] = Object.class;
			}
		}
		return elementTypes;
	}

	protected IPropertyAccessor createPropertyAccessor() {
		return new MethodAccessor(this, readMethod, writeMethod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IPropertyDescriptor#getPropertyAccessor()
	 */
	public IPropertyAccessor getPropertyAccessor() {
		if (propertyAccessor == null) {
			propertyAccessor = createPropertyAccessor();
		}
		return propertyAccessor;
	}

}
