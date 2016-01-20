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

import org.jmetis.kernel.metadata.IPropertyAccessor;
import org.jmetis.test.model.TestBean;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * {@code PropertyAccessorTest}
 * 
 * @author aerlach
 */
public abstract class PropertyAccessorTest {

	protected TestBean testBean;

	/**
	 * Constructs a new {@code PropertyAccessorTest} instance.
	 */
	public PropertyAccessorTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		testBean = new TestBean();
	}

	@After
	public void tearDown() throws Exception {
		testBean = null;
	}

	protected Class<?> beanClass() {
		return testBean.getClass();
	}

	protected abstract IPropertyAccessor propertyAccessorFor(String propertyName)
			throws Exception;

	@Test
	public void modifyBooleanProperty() throws Exception {
		IPropertyAccessor propertyAccessor = propertyAccessorFor("booleanField");
		Assert.assertEquals(false, propertyAccessor.getValueFrom(testBean));
		propertyAccessor.setValueInto(true, testBean);
		Assert.assertEquals(true, propertyAccessor.getValueFrom(testBean));
	}

	@Test
	public void modifyByteProperty() throws Exception {
		IPropertyAccessor propertyAccessor = propertyAccessorFor("byteField");
		Assert.assertEquals(0, propertyAccessor.getValueFrom(testBean));
		propertyAccessor.setValueInto((byte) 1, testBean);
		Assert.assertEquals(1, propertyAccessor.getValueFrom(testBean));
	}

	@Test
	public void modifyCharProperty() throws Exception {
		IPropertyAccessor propertyAccessor = propertyAccessorFor("charField");
		Assert.assertEquals('\0', propertyAccessor.getValueFrom(testBean));
		propertyAccessor.setValueInto('1', testBean);
		Assert.assertEquals('1', propertyAccessor.getValueFrom(testBean));
	}

	@Test
	public void modifyDoubleProperty() throws Exception {
		IPropertyAccessor propertyAccessor = propertyAccessorFor("doubleField");
		Assert.assertEquals(0.0, propertyAccessor.getValueFrom(testBean));
		propertyAccessor.setValueInto(1.0, testBean);
		Assert.assertEquals(1.0, propertyAccessor.getValueFrom(testBean));
	}

	@Test
	public void modifyFloatProperty() throws Exception {
		IPropertyAccessor propertyAccessor = propertyAccessorFor("floatField");
		Assert.assertEquals(0.0F, propertyAccessor.getValueFrom(testBean));
		propertyAccessor.setValueInto(1.0F, testBean);
		Assert.assertEquals(1.0F, propertyAccessor.getValueFrom(testBean));
	}

	@Test
	public void modifyIntProperty() throws Exception {
		IPropertyAccessor propertyAccessor = propertyAccessorFor("intField");
		Assert.assertEquals(0, propertyAccessor.getValueFrom(testBean));
		propertyAccessor.setValueInto(1, testBean);
		Assert.assertEquals(1, propertyAccessor.getValueFrom(testBean));
	}

	@Test
	public void modifyLongProperty() throws Exception {
		IPropertyAccessor propertyAccessor = propertyAccessorFor("longField");
		Assert.assertEquals(0, propertyAccessor.getValueFrom(testBean));
		propertyAccessor.setValueInto(1, testBean);
		Assert.assertEquals(1, propertyAccessor.getValueFrom(testBean));
	}

	@Test
	public void modifyShortProperty() throws Exception {
		IPropertyAccessor propertyAccessor = propertyAccessorFor("shortField");
		Assert.assertEquals((short) 0, propertyAccessor.getValueFrom(testBean));
		propertyAccessor.setValueInto((short) 1, testBean);
		Assert.assertEquals((short) 1, propertyAccessor.getValueFrom(testBean));
	}

	@Test
	public void modifyObjectProperty() throws Exception {
		IPropertyAccessor propertyAccessor = propertyAccessorFor("objectField");
		Assert.assertNull(propertyAccessor.getValueFrom(testBean));
		propertyAccessor.setValueInto(this, testBean);
		Assert.assertSame(this, propertyAccessor.getValueFrom(testBean));
	}

	@Test
	public void modifyStringProperty() throws Exception {
		IPropertyAccessor propertyAccessor = propertyAccessorFor("stringField");
		Assert.assertNull(propertyAccessor.getValueFrom(testBean));
		propertyAccessor.setValueInto("1", testBean);
		Assert.assertSame("1", propertyAccessor.getValueFrom(testBean));
	}

}
