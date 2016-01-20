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

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;

import org.jmetis.observable.collection.IndexedCollection;
import org.jmetis.test.PropertyChangeCollector;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@code IndexedModelTest}
 * 
 * @author aerlach
 */
public class IndexedModelTest {

	/**
	 * Constructs a new {@code IndexedModelTest} instance.
	 */
	public IndexedModelTest() {
		super();
	}

	protected void assertArrayEquals(Object[] expectedArray, Object actualValue) {
		Assert.assertTrue(actualValue instanceof Object[]);
		Object[] actualArray = (Object[]) actualValue;
		Assert.assertEquals(expectedArray.length, actualArray.length);
		for (int i = 0, n = expectedArray.length; i < n; i++) {
			Assert.assertEquals(expectedArray[i], actualArray[i]);
		}
	}

	protected void assertEventEquals(PropertyChangeEvent propertyChangeEvent,
			int index, Object[] oldArray, Object[] newArray) {
		Assert.assertEquals("elements", propertyChangeEvent.getPropertyName());
		IndexedPropertyChangeEvent indexedPropertyChangeEvent = (IndexedPropertyChangeEvent) propertyChangeEvent;
		Assert.assertEquals(index, indexedPropertyChangeEvent.getIndex());
		assertArrayEquals(oldArray, propertyChangeEvent.getOldValue());
		assertArrayEquals(newArray, propertyChangeEvent.getNewValue());
	}

	@Test
	public void validatePropertyChangeEvents() throws Exception {
		Integer zero = new Integer(0);
		Integer one = new Integer(1);
		Integer two = new Integer(2);
		IIndexedModel<Integer> observableList = new IndexedCollection<Integer>(
				new ArrayList<Integer>());
		PropertyChangeCollector propertyChangeCollector = new PropertyChangeCollector();
		observableList.addPropertyChangeListener(propertyChangeCollector);
		observableList.add(zero);
		Assert.assertTrue(observableList.contains(zero));
		Assert.assertEquals(1, propertyChangeCollector.numberOfEvents());
		assertEventEquals(propertyChangeCollector.lastPropertyChangeEvent(), 0,
				new Object[0], new Object[] { zero });
		observableList.add(one);
		Assert.assertTrue(observableList.contains(one));
		Assert.assertEquals(2, propertyChangeCollector.numberOfEvents());
		assertEventEquals(propertyChangeCollector.lastPropertyChangeEvent(), 1,
				new Object[0], new Object[] { one });
		observableList.set(1, two);
		Assert.assertFalse(observableList.contains(one));
		Assert.assertTrue(observableList.contains(two));
		Assert.assertEquals(3, propertyChangeCollector.numberOfEvents());
		assertEventEquals(propertyChangeCollector.lastPropertyChangeEvent(), 1,
				new Object[] { one }, new Object[] { two });
		observableList.remove(1);
		Assert.assertFalse(observableList.contains(two));
		Assert.assertEquals(4, propertyChangeCollector.numberOfEvents());
		assertEventEquals(propertyChangeCollector.lastPropertyChangeEvent(), 1,
				new Object[] { two }, new Object[0]);
		observableList.add(0, two);
		Assert.assertTrue(observableList.contains(two));
		Assert.assertEquals(5, propertyChangeCollector.numberOfEvents());
		assertEventEquals(propertyChangeCollector.lastPropertyChangeEvent(), 0,
				new Object[0], new Object[] { two });
		observableList.clear();
		Assert.assertFalse(observableList.contains(zero));
		Assert.assertFalse(observableList.contains(one));
		Assert.assertFalse(observableList.contains(two));
		Assert.assertEquals(6, propertyChangeCollector.numberOfEvents());
		assertEventEquals(propertyChangeCollector.lastPropertyChangeEvent(), 0,
				new Object[] { two, zero }, new Object[0]);
		observableList.addAll(Arrays.asList(new Integer[] { zero, one, two }));
		Assert.assertTrue(observableList.contains(zero));
		Assert.assertTrue(observableList.contains(one));
		Assert.assertTrue(observableList.contains(two));
		Assert.assertEquals(7, propertyChangeCollector.numberOfEvents());
		assertEventEquals(propertyChangeCollector.lastPropertyChangeEvent(), 0,
				new Object[0], new Object[] { zero, one, two });
	}

}
