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

import org.jmetis.kernel.assertion.Assertions;

/**
 * {@code ResourceLocatorDecorator}
 * 
 * @author aerlach
 */
public class ResourceLocatorDecorator implements IResourceLocator {

	private IResourceLocator decoratedResourceLocator;

	/**
	 * Constructs a new {@code ResourceLocatorDecorator} instance.
	 */
	public ResourceLocatorDecorator(IResourceLocator decoratedResourceLocator) {
		super();
		this.decoratedResourceLocator = Assertions.mustNotBeNull(
				"decoratedResourceLocator", decoratedResourceLocator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.resource.IResourceLocator#resolveResource(java.lang.String)
	 */
	@Override
	public URL resolveResource(String name) throws IOException {
		return this.decoratedResourceLocator.resolveResource(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.resource.IResourceLocator#resolveResources(java.lang.String)
	 */
	@Override
	public Enumeration<URL> resolveResources(String name) throws IOException {
		return this.decoratedResourceLocator.resolveResources(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.resource.IResourceLocator#loadClass(java.lang.String)
	 */
	@Override
	public Class<?> resolveClass(String name) throws ClassNotFoundException {
		return this.decoratedResourceLocator.resolveClass(name);
	}

}
