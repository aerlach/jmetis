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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.kernel.metadata.IMetaDataRegistry;
import org.jmetis.kernel.metadata.IPropertyDescription;
import org.jmetis.test.Primitives;
import org.jmetis.test.model.Address;
import org.jmetis.test.model.ConcreteClass;
import org.jmetis.test.model.Gender;
import org.jmetis.test.model.GenericClass;
import org.jmetis.test.model.InternationalAddress;
import org.jmetis.test.model.Person;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.util.tracker.ServiceTracker;

/**
 * {@code MetaDataRegistryTest}
 * 
 * @author aerlach
 */
public class MetaDataRegistryTest {

	private ServiceTracker metaDataRegistryTracker;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Primitives.ensureBundleStarted("org.jmetis.reflection");
		Bundle bundle = Primitives.ensureBundleStarted("org.jmetis.test");
		metaDataRegistryTracker = new ServiceTracker(bundle.getBundleContext(),
				IMetaDataRegistry.class.getName(), null);
		metaDataRegistryTracker.open();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		if (metaDataRegistryTracker != null) {
			metaDataRegistryTracker.close();
		}
	}

	protected IMetaDataRegistry metaDataRegistry() {
		return (IMetaDataRegistry) metaDataRegistryTracker.getService();
	}

	@Test
	public void metaDataRegistryIsAvailable() throws Exception {
		Assert.assertNotNull(metaDataRegistry());
	}

	protected IClassDescription metaDataOf(Class<?> classToDescribe)
			throws Exception {
		return metaDataRegistry().classDescriptorOf(classToDescribe);
	}

	@Test
	public void personMetaDataIsNotNull() throws Exception {
		Assert.assertNotNull(metaDataOf(Person.class));
	}

	@Test
	public void addressMetaDataIsNotNull() throws Exception {
		Assert.assertNotNull(metaDataOf(Address.class));
	}

	protected void assertValidPropertyMetaData(
			IClassDescription classDescriptor, String propertyName,
			Class<?> propertyType, boolean readOnly, String annotationValue,
			Type... typeArguments) {
		IPropertyDescription propertyDescriptor = classDescriptor
				.getPropertyDescriptorNamed(propertyName);
		Assert.assertNotNull(propertyDescriptor);
		Assert.assertEquals(propertyName, propertyDescriptor.getPropertyName());
		Assert.assertSame(propertyType, propertyDescriptor.getPropertyType());
		Assert.assertEquals(readOnly, propertyDescriptor.getPropertyAccessor()
				.isReadOnlyFor(null));
		if (typeArguments != null && typeArguments.length > 0) {
			Type[] propertyTypeArguments = propertyDescriptor.getElementTypes();
			Assert.assertEquals(typeArguments.length,
					propertyTypeArguments.length);
			for (int i = 0, n = typeArguments.length; i < n; i++) {
				Assert.assertSame(typeArguments[i], propertyTypeArguments[i]);
			}
		}

	}

	protected void assertValidPropertyMetaData(
			IClassDescription classDescriptor, String propertyName,
			Class<?> propertyType, boolean readOnly, Type... typeArguments) {
		this.assertValidPropertyMetaData(classDescriptor, propertyName,
				propertyType, readOnly, null, typeArguments);
	}

	protected void assertSupportedPropertyMetaData(
			IClassDescription classDescriptor, String... expectedPropertyNames) {
		Set<String> expectedSet = new HashSet<String>(Arrays
				.asList(expectedPropertyNames));
		String[] actualPropertyNames = classDescriptor.allPropertyNames();
		Set<String> actualSet = new HashSet<String>(Arrays
				.asList(actualPropertyNames));
		expectedSet.removeAll(actualSet);
		Assert.assertTrue(expectedSet.isEmpty());
	}

	@Test
	public void validatePersonMetaData() throws Exception {
		IClassDescription classDescriptor = metaDataOf(Person.class);
		assertSupportedPropertyMetaData(classDescriptor, "firstName",
				"lastName", "dayOfBirth", "age", "defaultAddress", "addresses",
				"salary", "gender", "manager");
		this.assertValidPropertyMetaData(classDescriptor, "firstName",
				String.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "lastName",
				String.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "dayOfBirth",
				Date.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "age", int.class,
				true);
		this.assertValidPropertyMetaData(classDescriptor, "defaultAddress",
				Address.class, true);
		this.assertValidPropertyMetaData(classDescriptor, "addresses",
				Collection.class, false, Address.class);
		this.assertValidPropertyMetaData(classDescriptor, "salary",
				BigDecimal.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "gender",
				Gender.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "manager",
				boolean.class, false);
	}

	@Test
	public void validateAddressMetaData() throws Exception {
		IClassDescription classDescriptor = metaDataOf(Address.class);
		assertSupportedPropertyMetaData(classDescriptor, "city", "code",
				"street", "number", "main");
		this.assertValidPropertyMetaData(classDescriptor, "city", String.class,
				false);
		this.assertValidPropertyMetaData(classDescriptor, "code", String.class,
				false);
		this.assertValidPropertyMetaData(classDescriptor, "street",
				String.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "number",
				String.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "main",
				boolean.class, false);
	}

	protected void assertInternationalAddressMetaData(
			IClassDescription classDescriptor) {
		assertSupportedPropertyMetaData(classDescriptor, "city", "code",
				"street", "number", "main", "country");
		this.assertValidPropertyMetaData(classDescriptor, "city", String.class,
				false);
		this.assertValidPropertyMetaData(classDescriptor, "code", String.class,
				false);
		this.assertValidPropertyMetaData(classDescriptor, "street",
				String.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "number",
				String.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "main",
				boolean.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "country",
				String.class, false);
	}

	@Test
	public void validateInternationalAddressMetaData() throws Exception {
		IClassDescription classDescriptor = metaDataOf(InternationalAddress.class);
		Assert.assertNotNull(classDescriptor);
		assertSupportedPropertyMetaData(classDescriptor, "city", "code",
				"street", "number", "main", "country");
		this.assertValidPropertyMetaData(classDescriptor, "city", String.class,
				false);
		this.assertValidPropertyMetaData(classDescriptor, "code", String.class,
				false);
		this.assertValidPropertyMetaData(classDescriptor, "street",
				String.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "number",
				String.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "main",
				boolean.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "country",
				String.class, false);
	}

	@Test
	public void validateInternationalAddressSuperclassMetaData()
			throws Exception {
		IClassDescription classDescriptor = metaDataOf(InternationalAddress.class);
		Assert.assertNotNull(classDescriptor);
		Assert.assertNotNull(classDescriptor.getSuperclassDescriptor());
		assertSupportedPropertyMetaData(classDescriptor, "city", "code",
				"street", "number", "main");
		this.assertValidPropertyMetaData(classDescriptor, "city", String.class,
				false);
		this.assertValidPropertyMetaData(classDescriptor, "code", String.class,
				false);
		this.assertValidPropertyMetaData(classDescriptor, "street",
				String.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "number",
				String.class, false);
		this.assertValidPropertyMetaData(classDescriptor, "main",
				boolean.class, false);
	}

	@Test
	public void validateGenericClassMetaDeta() throws Exception {
		IClassDescription classDescriptor = metaDataOf(GenericClass.class);
		System.err.println(classDescriptor);
		System.err.println(classDescriptor.getDescribedClass());
		System.err.println(Arrays.asList(classDescriptor.getDescribedClass()
				.getTypeParameters()));
		System.err.println(Arrays.asList(GenericClass.class
				.getGenericInterfaces()));
		System.err.println(Arrays.asList(GenericClass.class
				.getGenericSuperclass()));
		System.err.println(Arrays
				.asList(GenericClass.class.getTypeParameters()));

		classDescriptor = metaDataOf(ConcreteClass.class);
		System.err.println(classDescriptor);
		System.err.println(classDescriptor.getDescribedClass());
		System.err.println(Arrays.asList(classDescriptor.getDescribedClass()
				.getTypeParameters()));
		System.err.println(Arrays.asList(ConcreteClass.class
				.getGenericInterfaces()));
		System.err.println(Arrays.asList(ConcreteClass.class
				.getGenericSuperclass()));
		Type genericSuperclass = ConcreteClass.class.getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			System.err.println(Arrays
					.asList(((ParameterizedType) genericSuperclass)
							.getActualTypeArguments()));
		}

	}

}
