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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.jmetis.kernel.metadata.IPropertyAccessor;
import org.jmetis.reflection.property.MethodAccessor;
import org.junit.Assert;

/**
 * {@code MethodAccessorTest}
 * 
 * @author aerlach
 */
public class MethodAccessorTest extends PropertyAccessorTest {

	/**
	 * Constructs a new {@code MethodAccessorTest} instance.
	 */
	public MethodAccessorTest() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.mic.kernel.core.reflection.PropertyAdapterTest#propertyAdapterFor(
	 * java.lang.String)
	 */
	@Override
	protected IPropertyAccessor propertyAccessorFor(String propertyName)
			throws Exception {
		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass());
		for (PropertyDescriptor propertyDescriptor : beanInfo
				.getPropertyDescriptors()) {
			if (propertyName.equals(propertyDescriptor.getName())) {
				return new MethodAccessor(new DummyPropertyDescriptor(),
						propertyDescriptor.getReadMethod(), propertyDescriptor
								.getWriteMethod());
			}
		}
		Assert.fail();
		return null;
	}

}
