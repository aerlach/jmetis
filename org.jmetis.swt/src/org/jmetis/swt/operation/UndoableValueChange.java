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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * {@code UndoableValueChange}
 * 
 * @author aerlach
 */
public class UndoableValueChange<V> extends AbstractOperation {

	private IChangeTracker<V> changeTracker;

	private V oldValue;

	private V newValue;

	/**
	 * Constructs a new {@code UndoableValueChange} instance.
	 * 
	 * @param label
	 * @param undoManager
	 */
	public UndoableValueChange(String label, IChangeTracker<V> changeTracker) {
		super(label);
		this.changeTracker = changeTracker;
	}

	V getOldValue() {
		return this.oldValue;
	}

	void setOldValue(V oldValue) {
		this.oldValue = oldValue;
	}

	V getNewValue() {
		return this.newValue;
	}

	void setNewValue(V newValue) {
		this.newValue = newValue;
	}

	protected IStatus executeWith(V value) {
		this.changeTracker.setValue(value);
		return Status.OK_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.operations.AbstractOperation#execute(org.eclipse
	 * .core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return Status.OK_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.operations.AbstractOperation#redo(org.eclipse
	 * .core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		System.out.println("REDO old=" + this.oldValue + " , new="
				+ this.newValue);
		return this.executeWith(this.newValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.operations.AbstractOperation#undo(org.eclipse
	 * .core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		System.out.println("UNDO old=" + this.oldValue + " , new="
				+ this.newValue);
		return this.executeWith(this.oldValue);
	}

}
