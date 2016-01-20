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

import java.beans.PropertyChangeListener;

/**
 * {@code PropertyChangeDispatcherDecorator}
 * 
 * @author aerlach
 */
public class PropertyChangeDispatcherDecorator implements
IPropertyChangeDispatcher {

	private final IPropertyChangeDispatcher decoratedPropertyChangeDispatcher;

	/**
	 * Constructs a new {@code PropertyChangeDispatcherDecorator} instance.
	 * 
	 * @param decoratedPropertyChangeDispatcher
	 */
	public PropertyChangeDispatcherDecorator(
			IPropertyChangeDispatcher decoratedPropertyChangeDispatcher) {
		super();
		this.decoratedPropertyChangeDispatcher = decoratedPropertyChangeDispatcher;
	}

	/**
	 * Constructs a new {@code PropertyChangeDispatcherDecorator} instance.
	 */
	public PropertyChangeDispatcherDecorator() {
		this(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * addPropertyChangeListener(java.lang.String,
	 * java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeDispatcher addPropertyChangeListener(
			String propertyName, PropertyChangeListener propertyChangeListener) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher.addPropertyChangeListener(
					propertyName, propertyChangeListener);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeDispatcher addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (this.decoratedPropertyChangeDispatcher == null) {
			this.decoratedPropertyChangeDispatcher
			.addPropertyChangeListener(propertyChangeListener);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, java.lang.Object,
	 * java.lang.Object)
	 */
	public void firePropertyChangeEvent(String propertyName, Object oldValue,
			Object newValue) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher.firePropertyChangeEvent(
					propertyName, oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, boolean, boolean)
	 */
	public void firePropertyChangeEvent(String propertyName, boolean oldValue,
			boolean newValue) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher.firePropertyChangeEvent(
					propertyName, oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, byte, byte)
	 */
	public void firePropertyChangeEvent(String propertyName, byte oldValue,
			byte newValue) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher.firePropertyChangeEvent(
					propertyName, oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, char, char)
	 */
	public void firePropertyChangeEvent(String propertyName, char oldValue,
			char newValue) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher.firePropertyChangeEvent(
					propertyName, oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, double, double)
	 */
	public void firePropertyChangeEvent(String propertyName, double oldValue,
			double newValue) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher.firePropertyChangeEvent(
					propertyName, oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, float, float)
	 */
	public void firePropertyChangeEvent(String propertyName, float oldValue,
			float newValue) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher.firePropertyChangeEvent(
					propertyName, oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, int, int)
	 */
	public void firePropertyChangeEvent(String propertyName, int oldValue,
			int newValue) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher.firePropertyChangeEvent(
					propertyName, oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, long, long)
	 */
	public void firePropertyChangeEvent(String propertyName, long oldValue,
			long newValue) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher.firePropertyChangeEvent(
					propertyName, oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, short, short)
	 */
	public void firePropertyChangeEvent(String propertyName, short oldValue,
			short newValue) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher.firePropertyChangeEvent(
					propertyName, oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * removePropertyChangeListener(java.lang.String,
	 * java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeDispatcher removePropertyChangeListener(
			String propertyName, PropertyChangeListener propertyChangeListener) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher
			.removePropertyChangeListener(propertyName,
					propertyChangeListener);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeDispatcher removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (this.decoratedPropertyChangeDispatcher != null) {
			this.decoratedPropertyChangeDispatcher
			.removePropertyChangeListener(propertyChangeListener);
		}
		return this;
	}

}
