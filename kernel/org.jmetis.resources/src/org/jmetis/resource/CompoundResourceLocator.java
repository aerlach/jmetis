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

import org.jmetis.collections.enumeration.CompoundEnumeration;

/**
 * {@code CompoundResourceLocator}
 * 
 * @author aerlach
 */
public class CompoundResourceLocator implements IResourceLocator {

	private IResourceLocator[] resourceLocators;

	/**
	 * Constructs a new {@code CompoundResourceLocator} instance.
	 */
	public CompoundResourceLocator(IResourceLocator... resourceLocators) {
		super();
		this.resourceLocators = resourceLocators;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.resource.IResourceLocator#resolveResource(java.lang.String)
	 */
	@Override
	public URL resolveResource(String name) throws IOException {
		IOException exception = null;
		for (IResourceLocator currentLocator : this.resourceLocators) {
			try {
				URL resolvedResource = currentLocator.resolveResource(name);
				if (resolvedResource != null) {
					return resolvedResource;
				}
			} catch (IOException ex) {
				if (exception == null) {
					exception = ex;
				}
			}
		}
		if (exception != null) {
			throw exception;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.resource.IResourceLocator#resolveResources(java.lang.String)
	 */
	@Override
	public Enumeration<URL> resolveResources(String name) throws IOException {
		IOException exception = null;
		Enumeration<URL> compoundResources = null;
		for (IResourceLocator currentLocator : this.resourceLocators) {
			try {
				Enumeration<URL> resolvedResources = currentLocator
						.resolveResources(name);
				if (compoundResources != null) {
					compoundResources = new CompoundEnumeration<URL>(
							compoundResources, resolvedResources);
				} else {
					compoundResources = resolvedResources;
				}
			} catch (IOException ex) {
				if (exception == null) {
					exception = ex;
				}
			}
		}
		if (exception != null && compoundResources == null) {
			throw exception;
		}
		return compoundResources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.resource.IResourceLocator#resolveClass(java.lang.String)
	 */
	@Override
	public Class<?> resolveClass(String name) throws ClassNotFoundException {
		ClassNotFoundException exception = null;
		for (IResourceLocator currentLocator : this.resourceLocators) {
			try {
				return currentLocator.resolveClass(name);
			} catch (ClassNotFoundException ex) {
				if (exception == null) {
					exception = ex;
				}
			}
		}
		if (exception != null) {
			throw exception;
		}
		throw new ClassNotFoundException(name);
	}

}
