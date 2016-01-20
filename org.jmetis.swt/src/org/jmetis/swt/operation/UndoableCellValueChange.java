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


/**
 * {@code UndoableCellValueChange}
 * 
 * @author aerlach
 */
public class UndoableCellValueChange<V> extends UndoableValueChange<V> {

	private Object element;

	/**
	 * Constructs a new {@code UndoableCellValueChange} instance.
	 * 
	 * @param label
	 * @param controlAdapter
	 * @param element
	 */
	public UndoableCellValueChange(String label,
			IChangeTracker<V> changeTracker, Object element) {
		super(label, changeTracker);
		this.element = element;
	}

}
