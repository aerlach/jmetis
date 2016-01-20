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
package org.jmetis.observable;

import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.kernel.metadata.IMetaDataRegistry;
import org.jmetis.kernel.metadata.IPropertyDescription;
import org.jmetis.observable.object.BeanAdapter;
import org.jmetis.test.Primitives;
import org.jmetis.test.PropertyChangeCollector;
import org.jmetis.test.model.Person;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.util.tracker.ServiceTracker;

/**
 * {@code PropertyModelTest}
 * 
 * @author aerlach
 */
public class PropertyModelTest {

	private ServiceTracker metaDataRegistryTracker;

	/**
	 * Constructs a new {@code PropertyModelTest} instance.
	 */
	public PropertyModelTest() {
		super();
	}

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

	protected IMetaDataRegistry getMetaDataRegistry() {
		return (IMetaDataRegistry) metaDataRegistryTracker.getService();
	}

	protected IClassDescription getClassMetaDataOf(Class<?> classToDescribe)
			throws Exception {
		return getMetaDataRegistry().classDescriptorOf(classToDescribe);
	}

	protected IPropertyDescription getPropertyMetaData(Class<?> describedClass,
			String propertyName) throws Exception {
		return getClassMetaDataOf(describedClass).getPropertyDescriptorNamed(
				propertyName);
	}

	@Test
	public void validatePropertyChangeEvents() throws Exception {
		Person[] persons = Person.createSampleData("0").toArray(new Person[0]);
		IPropertyModel personModel = new BeanAdapter(
				getClassMetaDataOf(Person.class));
		IPropertyModel addressModel = personModel
				.getPropertyNamed("defaultAddress");
		IPropertyModel streetModel = addressModel.getPropertyNamed("street");
		IPropertyModel cityModel = addressModel.getPropertyNamed("city");
		PropertyChangeCollector addressChangeCollector = new PropertyChangeCollector();
		PropertyChangeCollector streetChangeCollector = new PropertyChangeCollector();
		PropertyChangeCollector cityChangeCollector = new PropertyChangeCollector();
		addressModel.addPropertyChangeListener(addressChangeCollector);
		streetModel.addPropertyChangeListener(streetChangeCollector);
		cityModel.addPropertyChangeListener(cityChangeCollector);
		personModel.setValue(persons[0]);
		Assert.assertEquals(1, addressChangeCollector.numberOfEvents());
		Assert.assertEquals(1, streetChangeCollector.numberOfEvents());
		Assert.assertEquals(1, cityChangeCollector.numberOfEvents());
		Assert.assertEquals("Bl�tenstrasse0", streetModel.getValue());
		personModel.setValue(persons[1]);
		Assert.assertEquals(2, addressChangeCollector.numberOfEvents());
		Assert.assertEquals(2, streetChangeCollector.numberOfEvents());
		Assert.assertEquals(2, cityChangeCollector.numberOfEvents());
		Assert.assertEquals("Bl�tenstrasse1", streetModel.getValue());
		persons[1].getDefaultAddress().setStreet("Test");
		Assert.assertEquals(3, streetChangeCollector.numberOfEvents());
	}
}
