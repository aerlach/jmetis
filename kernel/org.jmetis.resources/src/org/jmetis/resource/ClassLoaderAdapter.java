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

import org.jmetis.collections.enumeration.EmptyEnumeration;
import org.jmetis.kernel.assertion.Assertions;

/**
 * {@code ClassLoaderAdapter}
 * 
 * @author aerlach
 */
public class ClassLoaderAdapter implements IResourceLocator {

	private ClassLoader classLoader;

	/**
	 * Constructs a new {@code ClassLoaderAdapter} instance on the given {@code
	 * classLoader class loader}.
	 * 
	 * @param classLoader
	 */
	public ClassLoaderAdapter(ClassLoader classLoader) {
		super();
		this.classLoader = Assertions.mustNotBeNull("classLoader", classLoader); //$NON-NLS-1$
	}

	/**
	 * Constructs a new {@code ClassLoaderAdapter} instance on the current
	 * context class loader.
	 * 
	 * @see Thread#getContextClassLoader()
	 */
	protected ClassLoaderAdapter() {
		super();
	}

	protected ClassLoader currentClassLoader() {
		ClassLoader currentClassLoader = Thread.currentThread()
				.getContextClassLoader();
		if (currentClassLoader == null) {
			currentClassLoader = this.classLoader;
			if (currentClassLoader == null) {
				currentClassLoader = this.getClass().getClassLoader();
			}
		}
		return currentClassLoader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (this == otherObject) {
			return true;
		}
		if (otherObject instanceof ClassLoaderAdapter) {
			ClassLoaderAdapter otherResourceLoader = (ClassLoaderAdapter) otherObject;
			return this.classLoader != null
					&& this.classLoader.equals(otherResourceLoader.classLoader);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (this.classLoader != null) {
			return this.classLoader.hashCode();
		}
		return super.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.resources.IResourceLoader#resolveResource(java.lang
	 * .String)
	 */
	public URL resolveResource(String name) throws IOException {
		if (this.classLoader != null) {
			URL resolvedResource = this.classLoader.getResource(name);
			if (resolvedResource != null) {
				return resolvedResource;
			}
		}
		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		if (contextClassLoader != null) {
			return contextClassLoader.getResource(name);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.resources.IResourceLoader#resolveResources(java.lang
	 * .String)
	 */
	public Enumeration<URL> resolveResources(String name) throws IOException {
		if (this.classLoader != null) {
			Enumeration<URL> resolvedResources = this.classLoader
					.getResources(name);
			if (resolvedResources.hasMoreElements()) {
				return resolvedResources;
			}
		}
		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		if (contextClassLoader != null) {
			return contextClassLoader.getResources(name);
		}
		return EmptyEnumeration.singleInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.resources.IResourceLoader#loadClass(java.lang.String)
	 */
	public Class<?> resolveClass(String name) throws ClassNotFoundException {
		ClassNotFoundException exception = null;
		if (this.classLoader != null) {
			try {
				return this.classLoader.loadClass(name);
			} catch (ClassNotFoundException ex) {
				exception = ex;
			}
		}
		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		if (contextClassLoader != null) {
			try {
				return contextClassLoader.loadClass(name);
			} catch (ClassNotFoundException ex) {
				exception = ex;
			}
		}
		if (exception != null) {
			throw exception;
		}
		throw new ClassNotFoundException(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder().append("ClassLoaderAdapter(classLoader=") //$NON-NLS-1$
				.append(this.classLoader).append(')').toString();
	}

}
