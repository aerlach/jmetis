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

import java.lang.reflect.Field;

import org.jmetis.kernel.metadata.IPropertyAccessor;
import org.jmetis.reflection.property.FieldAccessor;

/**
 * {@code FieldAccessorTest}
 * 
 * @author aerlach
 */
public class FieldAccessorTest extends PropertyAccessorTest {

	/**
	 * Constructs a new {@code FieldAccessorTest} instance.
	 */
	public FieldAccessorTest() {
		super();
	}

	@Override
	protected IPropertyAccessor propertyAccessorFor(String propertyName)
			throws Exception {
		Field field = beanClass().getDeclaredField(propertyName);
		field.setAccessible(true);
		return new FieldAccessor(new DummyPropertyDescriptor(), field);
	}

}
