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
package org.jmetis.kernel.comparator;

import java.util.Comparator;

/**
 * {@code AffinityComparator}
 * 
 * @author era
 */
public class AffinityComparator implements Comparator<String> {

	private final IPhoneticCodeGenerator phoneticCodeGenerator;

	/**
	 * Constructs a new {@code AffinityComparator} instance.
	 * 
	 */
	public AffinityComparator(IPhoneticCodeGenerator phoneticCodeGenerator) {
		super();
		this.phoneticCodeGenerator = phoneticCodeGenerator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(String firstValue, String secondValue) {
		if (firstValue != secondValue) {
			String firstEncodedValue = this.phoneticCodeGenerator
					.toEncodedValue(firstValue);
			String secondEncodedValue = this.phoneticCodeGenerator
					.toEncodedValue(firstValue);
			return firstEncodedValue.compareTo(secondEncodedValue);
		}
		return 0;
	}

}