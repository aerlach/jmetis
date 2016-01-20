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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.Platform;
import org.jmetis.collections.enumeration.EmptyEnumeration;
import org.jmetis.kernel.assertion.Assertions;
import org.osgi.framework.Bundle;

/**
 * {@code BundleAdapter}
 * 
 * @author aerlach
 */
public class BundleAdapter implements IResourceLocator {

	private Bundle bundle;

	/**
	 * Constructs a new {@code BundleAdapter} instance for the given {@code
	 * bundle}. If the given {@code bundle} is {@code null} a
	 * {@link NullPointerException} is thrown.
	 * 
	 * @param bundle
	 *            the bundle to be used
	 */
	public BundleAdapter(Bundle bundle) {
		super();
		this.bundle = Assertions.mustNotBeNull("bundle", bundle); //$NON-NLS-1$
	}

	/**
	 * Constructs a new {@code BundleAdapter} instance for the bundle with the
	 * given {@code symbolicName}. If no resolved bundles are installed that
	 * have the given {@code symbolicName} then a {@link NullPointerException}
	 * is thrown.
	 * 
	 * @param symbolicName
	 *            the symbolic name of the bundle to be used
	 */
	public BundleAdapter(String symbolicName) {
		this(Platform.getBundle(Assertions.mustNotBeNull("symbolicName",
				symbolicName)));
	}

	/**
	 * Constructs a new {@code BundleAdapter} instance for the given {@code
	 * contributor}. If the given {@code contributor} is {@code null} a
	 * {@link NullPointerException} is thrown.
	 * 
	 * @param contributor
	 *            the contributor to be used
	 */
	public BundleAdapter(IContributor contributor) {
		this(Assertions.mustNotBeNull("contributor", contributor).getName()); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.resource.IResourceLocator#resolveClass(java.lang.String)
	 */
	public Class<?> resolveClass(String name) throws ClassNotFoundException {
		return this.bundle.loadClass(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.resource.IResourceLocator#resolveResource(java.lang.String)
	 */
	public URL resolveResource(String name) throws IOException {
		URL resolvedResource = this.bundle.getResource(name);
		if (resolvedResource != null) {
			resolvedResource = FileLocator.resolve(resolvedResource);
		}
		return resolvedResource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.resource.IResourceLocator#resolveResources(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Enumeration<URL> resolveResources(String name) throws IOException {
		Enumeration<URL> resolvedResources = this.bundle.getResources(name);
		if (resolvedResources != null) {
			return new URLEnumeration(resolvedResources);
		}
		return EmptyEnumeration.singleInstance();
	}

}
