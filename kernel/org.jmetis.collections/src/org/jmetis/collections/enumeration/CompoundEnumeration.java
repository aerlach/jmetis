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
package org.jmetis.collections.enumeration;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import org.jmetis.kernel.assertion.Assertions;

/**
 * {@code CompoundEnumeration}
 * 
 * @author era
 * @param <E>
 *            type of the items to iterate over
 */
public class CompoundEnumeration<E> implements Enumeration<E> {

	private final Enumeration<E>[] components;

	private int index;

	/**
	 * Constructs a new {@code CompoundEnumeration} instance.
	 * 
	 * @param components
	 */
	public CompoundEnumeration(Enumeration<E>... components) {
		this.components = Assertions.mustNotBeNullOrEmpty("components", //$NON-NLS-1$
				components);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Enumeration#hasMoreElements()
	 */
	public boolean hasMoreElements() {
		while (this.index < this.components.length) {
			if (this.components[this.index].hasMoreElements()) {
				return true;
			}
			this.index++;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Enumeration#nextElement()
	 */
	public E nextElement() throws NoSuchElementException {
		if (this.hasMoreElements()) {
			return this.components[this.index].nextElement();
		}
		throw new NoSuchElementException();
	}

}
