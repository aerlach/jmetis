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
package org.jmetis.resource;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import org.eclipse.core.runtime.FileLocator;
import org.jmetis.kernel.assertion.Assertions;

/**
 * {@code URLEnumeration}
 * 
 * @author aerlach
 */
class URLEnumeration implements Enumeration<URL> {

	private Enumeration<URL> resolvedResources;

	private URL currentResource;

	/**
	 * Constructs a new {@code URLEnumeration} instance.
	 */
	public URLEnumeration(Enumeration<URL> resolvedResources) {
		super();
		this.resolvedResources = Assertions.mustNotBeNull("resolvedResources",
				resolvedResources);
	}

	public boolean hasMoreElements() {
		while (this.currentResource == null
				&& this.resolvedResources.hasMoreElements()) {
			try {
				this.currentResource = FileLocator
						.resolve(this.resolvedResources.nextElement());
			} catch (IOException ex) {
				// Intentionally do nothing
			}
		}
		return this.currentResource != null;
	}

	public URL nextElement() {
		if (this.currentResource == null) {
			throw new NoSuchElementException();
		}
		try {
			return this.currentResource;
		} finally {
			this.currentResource = null;
		}
	}

}
