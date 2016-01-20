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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

/**
 * {@code CheckBoxAdapter}
 * 
 * @author aerlach
 */
public class CheckBoxAdapter extends ChangeTracker<Boolean, Button> {

	/**
	 * Constructs a new {@code CheckBoxAdapter} instance.
	 * 
	 * @param control
	 */
	public CheckBoxAdapter(String label, Button control) {
		super(label, control);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.ChangeTracker#startChangeTrackingFor(org.eclipse
	 * .swt .widgets.Control)
	 */
	@Override
	protected void startChangeTrackingFor(Button control) {
		control.addListener(SWT.Selection, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.ChangeTracker#stopChangeTrackingFor(org.eclipse
	 * .swt .widgets.Control)
	 */
	@Override
	protected void stopChangeTrackingFor(Button control) {
		control.removeListener(SWT.Selection, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.swt.operation.ChangeTracker#getValueFrom(org.eclipse.swt.
	 * widgets.Control)
	 */
	@Override
	public Boolean getValueFrom(Button control) {
		return control.getSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.ChangeTracker#setValueInto(java.lang.Object,
	 * org.eclipse.swt.widgets.Control)
	 */
	@Override
	public void setValueInto(Boolean value, Button control) {
		control.setSelection(value);
	}

}
