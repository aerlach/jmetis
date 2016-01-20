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

/**
 * {@code IControlUndoContext}
 * 
 * @author aerlach
 */
public interface IControlUndoContext {

	/**
	 * @param control
	 * @param changeTracker
	 */
	void addChangeTracker(Control control, IChangeTracker<?> changeTracker);

	/**
	 * @param control
	 * @param changeTracker
	 */
	void removeChangeTracker(Control control, IChangeTracker<?> changeTracker);

	/**
	 * @param <V>
	 * @param label
	 * @param changeTracker
	 * @param undoableOperation
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	<V> UndoableValueChange<V> updateUndoableOperation(String label,
			IChangeTracker<V> changeTracker,
			UndoableValueChange<V> undoableOperation, V oldValue, V newValue);

}
