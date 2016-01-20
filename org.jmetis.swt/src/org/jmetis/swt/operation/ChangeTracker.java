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
package org.jmetis.swt.operation;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * {@code ChangeTracker}
 * 
 * @author aerlach
 */
public abstract class ChangeTracker<V, C extends Control> implements
		IChangeTracker<V>, Listener {

	private String label;

	private C control;

	private IControlUndoContext controlUndoContext;

	private V oldValue;

	private UndoableValueChange<V> undoableOperation;

	/**
	 * Constructs a new {@code ChangeTracker} instance.
	 * 
	 * @param control
	 */
	public ChangeTracker(String label, C control) {
		super();
		// TODO FIELD NAME AS LABEL
		this.label = label;
		this.control = control;
	}

	protected abstract void startChangeTrackingFor(C control);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.swt.operation.IChangeTracker#startChangeTracking()
	 */
	public final void startChangeTrackingFor(
			IControlUndoContext controlUndoContext) {
		this.controlUndoContext = controlUndoContext;
		try {
			this.oldValue = this.getValueFrom(this.control);
		} finally {
			this.startChangeTrackingFor(this.control);
		}
	}

	protected abstract void stopChangeTrackingFor(C control);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.swt.operation.IChangeTracker#stopChangeTracking()
	 */
	public final void stopChangeTrackingFor(
			IControlUndoContext controlUndoContext) {
		try {
			this.stopChangeTrackingFor(this.control);
		} finally {
			this.oldValue = null;
			this.undoableOperation = null;
			this.controlUndoContext = null;
		}
	}

	protected abstract V getValueFrom(C control);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.swt.operation.IChangeTracker#getValue()
	 */
	public final V getValue() {
		return this.getValueFrom(this.control);
	}

	protected abstract void setValueInto(V value, C control);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.swt.operation.IChangeTracker#setValue(V)
	 */
	public final void setValue(V value) {
		try {
			this.setValueInto(value, this.control);
		} finally {
			this.control.setFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.IChangeTracker#handleEvent(org.eclipse.swt.widgets
	 * .Event)
	 */
	public void handleEvent(Event event) {
		this.controlUndoContext.updateUndoableOperation(this.label, this,
				this.undoableOperation, this.oldValue, this.getValue());
	}

}
