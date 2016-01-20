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
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * {@code CellEditorUndoContext}
 * 
 * @author aerlach
 */
public class CellEditorUndoContext extends DecoratingControlUndoContext
		implements ICellEditorUndoContext {

	private ColumnViewer columnViewer;

	private int columnIndex;

	/**
	 * Constructs a new {@code CellEditorUndoContext} instance.
	 * 
	 * @param operationHistory
	 * @param undoContext
	 * @param columnViewer
	 * @param columnIndex
	 * @param controlAdapter
	 */
	public CellEditorUndoContext(
			IControlUndoContext decoratedControlUndoContext,
			ColumnViewer columnViewer, int columnIndex) {
		super(decoratedControlUndoContext);
		this.columnViewer = columnViewer;
		this.columnIndex = columnIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.ICellEditorUndoContext#addChangeTracker(org.
	 * eclipse.jface.viewers.CellEditor,
	 * org.jmetis.swt.operation.IChangeTracker)
	 */
	@Override
	public void addChangeTracker(CellEditor cellEditor,
			IChangeTracker<?> changeTracker) {
		this.addChangeTracker(cellEditor.getControl(), changeTracker);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.ICellEditorUndoContext#removeChangeTracker(org
	 * .eclipse.jface.viewers.CellEditor,
	 * org.jmetis.swt.operation.IChangeTracker)
	 */
	@Override
	public void removeChangeTracker(CellEditor cellEditor,
			IChangeTracker<?> changeTracker) {
		this.removeChangeTracker(cellEditor.getControl(), changeTracker);
	}

	protected IStructuredSelection structuredSelection() {
		return (IStructuredSelection) this.columnViewer.getSelection();

	}

	protected Object selectedElement() {
		return this.structuredSelection().getFirstElement();
	}

	public <V> UndoableValueChange<V> createUndoableOperation(String label,
			IChangeTracker<V> changeTracker) {
		return new UndoableCellValueChange<V>(label, changeTracker, this
				.selectedElement());
	}

}
