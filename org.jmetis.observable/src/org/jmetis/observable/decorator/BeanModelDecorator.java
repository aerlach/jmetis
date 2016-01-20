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
package org.jmetis.observable.decorator;

import java.beans.PropertyChangeListener;

import org.jmetis.observable.IBeanModel;

/**
 * {@code BeanModelDecorator} allows new/additional behavior to be added to an
 * {@link IBeanModel} dynamically.
 * 
 * @author aerlach
 */
public class BeanModelDecorator implements IBeanModel {

	private IBeanModel decoratedBeanModel;

	/**
	 * Constructs a new {@code BeanModelDecorator} instance.
	 */
	public BeanModelDecorator(IBeanModel beanModel) {
		super();
		this.decoratedBeanModel = beanModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IBeanModel#addPropertyChangeListener(java.beans
	 * .PropertyChangeListener)
	 */
	public void addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		this.decoratedBeanModel
				.addPropertyChangeListener(propertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IBeanModel#addPropertyChangeListener(java.lang.
	 * String, java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener propertyChangeListener) {
		this.decoratedBeanModel.addPropertyChangeListener(propertyName,
				propertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IBeanModel#removePropertyChangeListener(java.beans
	 * .PropertyChangeListener)
	 */
	public void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		this.decoratedBeanModel
				.removePropertyChangeListener(propertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IBeanModel#removePropertyChangeListener(java.lang
	 * .String, java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener propertyChangeListener) {
		this.decoratedBeanModel.removePropertyChangeListener(propertyName,
				propertyChangeListener);
	}

}
