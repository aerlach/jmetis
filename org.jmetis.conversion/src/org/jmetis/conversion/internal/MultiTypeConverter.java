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
package org.jmetis.conversion.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.jmetis.conversion.ITypeConverter;

/**
 * {@code MultiTypeConverter}
 * 
 * @author aerlach
 */
public class MultiTypeConverter implements ITypeConverter {

	private Map<Class<?>, Map<Class<?>, ITypeConverter>> typeConverters;

	/**
	 * Constructs a new {@code MultiTypeConverter} instance.
	 */
	public MultiTypeConverter() {
		super();
		this.typeConverters = new HashMap<Class<?>, Map<Class<?>, ITypeConverter>>();
	}

	protected void addTypeConverterFor(ITypeConverter typeConverter,
			Class<?> sourceType, Class<?> targetType) {
		Map<Class<?>, ITypeConverter> typeConverters = this.typeConverters
				.get(sourceType);
		if (typeConverters == null) {
			typeConverters = new HashMap<Class<?>, ITypeConverter>();
			this.typeConverters.put(sourceType, typeConverters);
		}
		typeConverters.put(targetType, typeConverter);
	}

	protected void removeTypeConverterFor(ITypeConverter typeConverter,
			Class<?> sourceType, Class<?> targetType) {
		Map<Class<?>, ITypeConverter> typeConverters = this.typeConverters
				.get(sourceType);
		if (typeConverters != null) {
			typeConverters.remove(targetType);
		}
	}

	protected ITypeConverter typeConverterFor(Class<?> sourceType,
			Class<?> targetType) {
		Map<Class<?>, ITypeConverter> typeConverters = this.typeConverters
				.get(sourceType);
		if (typeConverters != null) {
			return typeConverters.get(targetType);
		}
		return null;
	}

	protected <T> T handleConverterNotFound(Object sourceValue,
			Class<?> sourceType, Class<T> targetType) {
		// TODO throw exception
		throw new NoSuchElementException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.conversion.ITypeConverter#convertTo(java.lang.Object,
	 * java.lang.Class)
	 */
	public <T> T convertTo(Object sourceValue, Class<T> targetType) {
		Class<?> sourceType;
		if (sourceValue != null) {
			sourceType = sourceValue.getClass();
		} else {
			sourceType = Object.class;
		}
		ITypeConverter typeConverter = this.typeConverterFor(sourceType,
				targetType);
		if (typeConverter == null) {
			return this.handleConverterNotFound(sourceValue, sourceType,
					targetType);
		}
		return typeConverter.convertTo(sourceValue, targetType);
	}

}
