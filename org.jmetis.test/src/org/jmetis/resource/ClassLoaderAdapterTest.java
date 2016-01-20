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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

public class ClassLoaderAdapterTest {

	public ClassLoaderAdapterTest() {
		super();
	}

	protected IResourceLocator createResourceLocator(ClassLoader classLoader) {
		return new ClassLoaderAdapter(classLoader);
	}

	protected ClassLoader defaultClassLoader() {
		return this.getClass().getClassLoader();
	}

	protected IResourceLocator createDefaultResourceLocator() {
		return this.createResourceLocator(this.defaultClassLoader());
	}

	@Test
	public void createResourceLocatorWithNonNullClassLoader() {
		this.createDefaultResourceLocator();
	}

	@Test(expected = NullPointerException.class)
	public void createResourceLocatorWithNullParameter() {
		this.createResourceLocator(null);
	}

	@Test
	public void resourceLocatorsAreEqualIfClassLoadersAreEqual() {
		Assert.assertEquals(this.createDefaultResourceLocator(), this
				.createDefaultResourceLocator());
	}

	@Test
	public void resourceLocatorsAreNotEqualIfClassLoadersAreNotEqual() {
		Assert
				.assertFalse(this.createDefaultResourceLocator().equals(
						this.createResourceLocator(ClassLoader
								.getSystemClassLoader())));
	}

	@Test
	public void hashCodesAreEqualIfClassLoadersAreEqual() {
		Assert.assertEquals(this.createDefaultResourceLocator().hashCode(),
				this.createDefaultResourceLocator().hashCode());
	}

	@Test
	public void hashCodesAreNotEqualIfClassLoadersAreNotEqual() {
		Assert
				.assertTrue(this.createDefaultResourceLocator().hashCode() != this
						.createResourceLocator(
								ClassLoader.getSystemClassLoader()).hashCode());
	}

	@Test
	public void resourceLocatorResolvesSameResourceAsClassLoader()
			throws IOException {
		ClassLoader classLoader = this.defaultClassLoader();
		URL classLoaderResource = classLoader
				.getResource("/org/jmetis/resource/Test.properties"); //$NON-NLS-1$
		IResourceLocator resourceLocator = this.createDefaultResourceLocator();
		URL resourceLocatorResource = resourceLocator
				.resolveResource("/org/jmetis/resource/Test.properties"); //$NON-NLS-1$
		Assert.assertEquals(classLoaderResource, resourceLocatorResource);
	}

	@Test
	public void resourceLocatorResolvesSameResourcesAsClassLoader()
			throws Exception {
		ClassLoader classLoader = this.defaultClassLoader();
		Collection<URL> classLoaderResources = new ArrayList<URL>(Collections
				.list(classLoader
						.getResources("/org/jmetis/resource/Test.properties"))); //$NON-NLS-1$
		IResourceLocator resourceLocator = this.createDefaultResourceLocator();
		Collection<URL> resourceLocatorResources = Collections
				.list(resourceLocator
						.resolveResources("/org/jmetis/resource/Test.properties")); //$NON-NLS-1$
		Assert.assertEquals(classLoaderResources.size(),
				resourceLocatorResources.size());
		for (URL url : resourceLocatorResources) {
			Assert.assertTrue(classLoaderResources.remove(url));
		}
		Assert.assertEquals(0, classLoaderResources.size());
	}

	@Test
	public void resourceLocatorLoadsSamleClassAsClassLoader() throws Exception {
		ClassLoader classLoader = this.defaultClassLoader();
		IResourceLocator resourceLocator = this.createDefaultResourceLocator();
		Assert.assertSame(classLoader.loadClass("org.jmetis.resource.Test"),
				resourceLocator.resolveClass("org.jmetis.resource.Test"));
	}

}
