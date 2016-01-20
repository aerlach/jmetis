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
package org.jmetis.observable.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * {@code NullPropertyChangeListener}
 * 
 * @author aerlach
 */
public class NullPropertyChangeListener implements IPropertyChangeListener {

	private static final IPropertyChangeListener DEFAULT_INSTANCE = new NullPropertyChangeListener();

	/**
	 * Constructs a new {@code NullPropertyChangeListener} instance.
	 */
	private NullPropertyChangeListener() {
		super();
	}

	public static IPropertyChangeListener defaultInstance() {
		return NullPropertyChangeListener.DEFAULT_INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeListener#
	 * addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeListener addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		return new CompoundPropertyChangeListener(propertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.internal.IPropertyChangeListener#propertyChangeEvent
	 * (java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeListener#
	 * removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeListener removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		return this;
	}

}
