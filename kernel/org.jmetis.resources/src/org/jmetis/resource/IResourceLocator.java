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

/**
 * {@code IResourceLocator} can be used for loading resources (e.g. class path or
 * file system resources). The default {@code ClassLoaderAdapter} uses the class
 * loader.
 * 
 * @author aerlach
 * @see java.lang.ClassLoader
 */
public interface IResourceLocator {

	IResourceLocator DEFAULT_INSTANCE = new ClassLoaderAdapter();

	/**
	 * Returns the resource with the given {@code name}. A resource is some data
	 * (images, audio, text, etc) that can be accessed by class code in a way
	 * that is independent of the location of the code.
	 * 
	 * @param name
	 *            the name of the resource
	 * @return the resource with the given {@code name}
	 * @throws IOException
	 *             TODO
	 * @see java.lang.ClassLoader#getResource(String)
	 */
	URL resolveResource(String name) throws IOException;

	/**
	 * Returns all the resources with the given {@code name}. A resource is some
	 * data (images, audio, text, etc) that can be accessed by class code in a
	 * way that is independent of the location of the code.
	 * 
	 * @param name
	 *            the name of the resource
	 * @return an enumeration of {@link java.net.URL} instances for the
	 *         resource. If no resources could be found, the enumeration will be
	 *         empty.
	 * @see java.lang.ClassLoader#getResources(String)
	 */
	Enumeration<URL> resolveResources(String name) throws IOException;

	/**
	 * Returns the {@link Class} with the given {@code name}.
	 * 
	 * @param name
	 *            the name of the class
	 * @return the {@link Class} object
	 * @throws ClassNotFoundException
	 *             if the class was not found
	 */
	Class<?> resolveClass(String name) throws ClassNotFoundException;

}
