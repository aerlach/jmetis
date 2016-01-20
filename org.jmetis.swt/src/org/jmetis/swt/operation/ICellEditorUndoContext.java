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

/**
 * {@code ICellEditorUndoContext}
 * 
 * @author aerlach
 */
public interface ICellEditorUndoContext extends IControlUndoContext {

	/**
	 * @param cellEditor
	 * @param changeTracker
	 */
	void addChangeTracker(CellEditor cellEditor, IChangeTracker<?> changeTracker);

	/**
	 * @param cellEditor
	 * @param changeTracker
	 */
	void removeChangeTracker(CellEditor cellEditor,
			IChangeTracker<?> changeTracker);

}
