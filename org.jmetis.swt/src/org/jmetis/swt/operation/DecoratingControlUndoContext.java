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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Control;

/**
 * {@code DecoratingControlUndoContext}
 * 
 * @author aerlach
 */
public class DecoratingControlUndoContext implements IControlUndoContext,
		ICellEditorUndoContext {

	private IControlUndoContext decoratedControlUndoContext;

	/**
	 * Constructs a new {@code DecoratingControlUndoContext} instance.
	 */
	public DecoratingControlUndoContext(
			IControlUndoContext decoratedControlUndoContext) {
		super();
		this.decoratedControlUndoContext = decoratedControlUndoContext;
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
		this.decoratedControlUndoContext.addChangeTracker(control,
				changeTracker);
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
		this.decoratedControlUndoContext.removeChangeTracker(control,
				changeTracker);
	}

	public void addChangeTracker(CellEditor cellEditor,
			IChangeTracker<?> changeTracker) {
		this.addChangeTracker(cellEditor.getControl(), changeTracker);
	}

	public void removeChangeTracker(CellEditor cellEditor,
			IChangeTracker<?> changeTracker) {
		this.removeChangeTracker(cellEditor.getControl(), changeTracker);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.IControlUndoContext#updateUndoableOperation(
	 * java.lang.String, org.jmetis.swt.operation.IChangeTracker,
	 * org.jmetis.swt.operation.UndoableValueChange, java.lang.Object,
	 * java.lang.Object)
	 */
	public <V> UndoableValueChange<V> updateUndoableOperation(String label,
			IChangeTracker<V> changeTracker,
			UndoableValueChange<V> undoableOperation, V oldValue, V newValue) {
		return this.decoratedControlUndoContext.updateUndoableOperation(label,
				changeTracker, undoableOperation, oldValue, newValue);
	}

}
