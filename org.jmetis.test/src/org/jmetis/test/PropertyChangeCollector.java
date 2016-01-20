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
package org.jmetis.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code PropertyChangeCollector}
 * 
 * @author aerlach
 */
public class PropertyChangeCollector implements PropertyChangeListener {

	private List<PropertyChangeEvent> propertyChangeEvents;

	/**
	 * Constructs a new {@code PropertyChangeCollector} instance.
	 */
	public PropertyChangeCollector() {
		super();
		propertyChangeEvents = new ArrayList<PropertyChangeEvent>();
	}

	public List<PropertyChangeEvent> propertyChangeEvents() {
		return propertyChangeEvents;
	}

	public int numberOfEvents() {
		return propertyChangeEvents.size();
	}

	public PropertyChangeEvent lastPropertyChangeEvent() {
		return propertyChangeEvents.get(propertyChangeEvents.size() - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		System.out.println("Source: " + propertyChangeEvent.getSource()
				+ " Name: " + propertyChangeEvent.getPropertyName()
				+ " Old-Value: " + propertyChangeEvent.getOldValue()
				+ " New-Value: " + propertyChangeEvent.getNewValue());
		propertyChangeEvents.add(propertyChangeEvent);
	}
}
