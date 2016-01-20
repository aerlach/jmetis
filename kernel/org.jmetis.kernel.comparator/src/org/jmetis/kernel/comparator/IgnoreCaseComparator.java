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
 * {@code IgnoreCaseComparator} compares two strings lexicographically, ignoring
 * case differences.
 * 
 * @author era
 * 
 * @see java.lang.String#compareToIgnoreCase(String)
 */
public class IgnoreCaseComparator implements Comparator<String> {

	private static Comparator<String> DEFAULT_INSTANCE;

	/**
	 * Constructs a new {@code IgnoreCaseComparator} instance.
	 * 
	 * @see org.jmetis.kernel.comparator.IgnoreCaseComparator#defaultInstance()
	 */
	public IgnoreCaseComparator() {
		super();
	}

	/**
	 * Returns the default {@code IgnoreCaseComparator} instance.
	 * 
	 * @return the default {@code IgnoreCaseComparator} instance.
	 */
	public static Comparator<String> defaultInstance() {
		if (IgnoreCaseComparator.DEFAULT_INSTANCE == null) {
			IgnoreCaseComparator.DEFAULT_INSTANCE = new IgnoreCaseComparator();
		}
		return IgnoreCaseComparator.DEFAULT_INSTANCE;
	}

	/**
	 * Compares two strings lexicographically, ignoring case differences.
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(String firstValue, String secondValue) {
		return firstValue.compareToIgnoreCase(secondValue);
	}

}
