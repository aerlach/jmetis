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
import org.eclipse.swt.widgets.Text;

/**
 * {@code TextAdapter}
 * 
 * @author aerlach
 */
public class TextAdapter extends ChangeTracker<String, Text> {

	/**
	 * Constructs a new {@code TextAdapter} instance.
	 * 
	 * @param label
	 * @param control
	 */
	public TextAdapter(String label, Text control) {
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
	protected void startChangeTrackingFor(Text control) {
		control.addListener(SWT.Modify, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.ChangeTracker#stopChangeTrackingFor(org.eclipse
	 * .swt .widgets.Control)
	 */
	@Override
	protected void stopChangeTrackingFor(Text control) {
		control.removeListener(SWT.Modify, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.ChangeTracker#getValueFrom(org.eclipse.swt.widgets
	 * .Control)
	 */
	@Override
	protected String getValueFrom(Text control) {
		return control.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.swt.operation.ChangeTracker#setValueInto(java.lang.Object,
	 * org.eclipse.swt.widgets.Control)
	 */
	@Override
	protected void setValueInto(String value, Text control) {
		control.setText(value);
	}

}
