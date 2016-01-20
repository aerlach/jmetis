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
package org.jmetis.kernel.factory;

import java.lang.reflect.Constructor;

/**
 * {@code TestableObjectFactory}
 * 
 * @author era
 */
public class TestableObjectFactory<T> extends ObjectFactory<T> {

	/**
	 * Constructs a new {@code TestableObjectFactory} instance.
	 * 
	 * @param objectType
	 * @param objectInitializers
	 */
	public TestableObjectFactory(Class<T> objectType,
			IObjectInitializer<T>... objectInitializers) {
		super(objectType, objectInitializers);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.factory.ObjectFactory#matchArgumentType(java.lang.Class
	 * , java.lang.Class)
	 */
	@Override
	public boolean matchArgumentType(Class<?> targetType, Class<?> sourceType) {
		return super.matchArgumentType(targetType, sourceType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.factory.ObjectFactory#matchArgumentTypes(java.lang.
	 * Class<?>[], java.lang.Class<?>[])
	 */
	@Override
	public boolean matchArgumentTypes(Class<?>[] targetTypes,
			Class<?>[] sourceTypes) {
		return super.matchArgumentTypes(targetTypes, sourceTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.factory.ObjectFactory#createMatchingConstructor(java
	 * .lang.Class<?>[])
	 */
	@Override
	public Constructor<T> createMatchingConstructor(Class<?>[] argumentTypes) {
		return super.createMatchingConstructor(argumentTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.factory.ObjectFactory#createConstructor(java.lang.Class
	 * <?>[])
	 */
	@Override
	public Constructor<T> createConstructor(Class<?>[] argumentTypes) {
		return super.createConstructor(argumentTypes);
	}

}
