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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * {@code ControlUndoContext}
 * 
 * @author aerlach
 */
public class ControlUndoContext implements Listener, IControlUndoContext {

	private IOperationHistory operationHistory;

	private IUndoContext undoContext;

	private Map<Control, IChangeTracker<?>> changeTrackers;

	/**
	 * Constructs a new {@code ControlUndoContext} instance.
	 * 
	 * @param operationHistory
	 * @param undoContext
	 * @param changeTracker
	 */
	public ControlUndoContext(IOperationHistory operationHistory,
			IUndoContext undoContext) {
		super();
		this.operationHistory = operationHistory;
		this.undoContext = undoContext;
		this.changeTrackers = new HashMap<Control, IChangeTracker<?>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.IControlUndoContext#addChangeTracker(org.eclipse
	 * .swt.widgets.Control, org.jmetis.swt.operation.IChangeTracker)
	 */
	public void addChangeTracker(Control control,
			IChangeTracker<?> changeTracker) {
		if (this.changeTrackers.put(control, changeTracker) != null) {
			control.removeListener(SWT.FocusIn, this);
			control.removeListener(SWT.FocusOut, this);
		}
		control.addListener(SWT.FocusIn, this);
		control.addListener(SWT.FocusOut, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.IControlUndoContext#removeChangeTracker(org.
	 * eclipse.swt.widgets.Control, org.jmetis.swt.operation.IChangeTracker)
	 */
	public void removeChangeTracker(Control control,
			IChangeTracker<?> changeTracker) {
		if (this.changeTrackers.remove(control) != changeTracker) {
			control.removeListener(SWT.FocusIn, this);
			control.removeListener(SWT.FocusOut, this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.IControlUndoContext#createUndoableOperation(
	 * java.lang.String, org.jmetis.swt.operation.IChangeTracker)
	 */
	protected <V> UndoableValueChange<V> createUndoableOperation(String label,
			IChangeTracker<V> changeTracker) {
		UndoableValueChange<V> undoableOperation = new UndoableValueChange<V>(
				label, changeTracker);
		undoableOperation.addContext(this.undoContext);
		this.operationHistory.add(undoableOperation);
		return undoableOperation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.IControlUndoContext#updateUndoableOperation(
	 * org.jmetis.swt.operation.UndoableValueChange, java.lang.Object)
	 */
	public <V> UndoableValueChange<V> updateUndoableOperation(String label,
			IChangeTracker<V> changeTracker,
			UndoableValueChange<V> undoableOperation, V oldValue, V newValue) {
		if (oldValue != newValue
				&& (oldValue == null || !oldValue.equals(newValue))) {
			if (undoableOperation == null) {
				undoableOperation = this.createUndoableOperation(label,
						changeTracker);
				undoableOperation.setOldValue(oldValue);
			}
			undoableOperation.setNewValue(newValue);
		} else if (undoableOperation != null) {
			undoableOperation.removeContext(this.undoContext);
		}
		return undoableOperation;
	}

	protected IChangeTracker<?> changeTrackerFor(Control control) {
		return this.changeTrackers.get(control);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent(Event event) {
		Control control = (Control) event.widget;
		switch (event.type) {
		case SWT.FocusIn:
			this.changeTrackerFor(control).startChangeTrackingFor(this);
			break;
		case SWT.FocusOut:
			this.changeTrackerFor(control).stopChangeTrackingFor(this);
			break;
		}
	}

}
