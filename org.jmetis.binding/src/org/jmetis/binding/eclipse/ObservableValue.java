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
package org.jmetis.binding.eclipse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.jmetis.binding.IObservableProperty;

/**
 * {@code ObservableValue}
 * 
 * @author aerlach
 */
public class ObservableValue extends AbstractObservableValue {

	private IObservableProperty baseObservableValue;

	private PropertyChangeListener valueChangeHandler;

	/**
	 * Constructs a new {@code ObservableValue} instance.
	 */
	public ObservableValue(IObservableProperty baseObservableValue) {
		super();
		this.baseObservableValue = baseObservableValue;
	}

	@Override
	protected void fireValueChange(ValueDiff diff) {
		super.fireValueChange(diff);
	}

	protected void forwardValueChanged(PropertyChangeEvent propertyChangeEvent) {
		if (hasListeners()) {
			final ValueDiff diff = Diffs.createValueDiff(propertyChangeEvent
					.getOldValue(), propertyChangeEvent.getNewValue());
			getRealm().exec(new Runnable() {
				public void run() {
					ObservableValue.this.fireValueChange(diff);
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.databinding.observable.ChangeManager#firstListenerAdded
	 * ()
	 */
	@Override
	protected void firstListenerAdded() {
		valueChangeHandler = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
				ObservableValue.this.forwardValueChanged(propertyChangeEvent);
			}
		};
		baseObservableValue.addPropertyChangeListener(valueChangeHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.databinding.observable.ChangeManager#lastListenerRemoved
	 * ()
	 */
	@Override
	protected void lastListenerRemoved() {
		baseObservableValue.removePropertyChangeListener(valueChangeHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.databinding.observable.value.IObservableValue#getValueType
	 * ()
	 */
	public Object getValueType() {
		return baseObservableValue.getValueType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.databinding.observable.value.AbstractObservableValue
	 * #doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		return baseObservableValue.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.databinding.observable.value.AbstractObservableValue
	 * #doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(Object value) {
		baseObservableValue.setValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.databinding.observable.value.AbstractObservableValue
	 * #dispose()
	 */
	@Override
	public synchronized void dispose() {
		lastListenerRemoved();
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return baseObservableValue.toString();
	}

}
