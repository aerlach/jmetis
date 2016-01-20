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
package org.jmetis.template.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.dynamichelpers.IExtensionChangeHandler;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.template.library.ITemplateHandlerLibrary;
import org.jmetis.template.library.ITemplateHandlerLibraryRegistry;
import org.osgi.framework.BundleContext;

/**
 * {@code TemplateHandlerLibraryExtensionChangeHandler}
 * 
 * @author aerlach
 */
class TemplateHandlerLibraryExtensionChangeHandler implements
		ITemplateHandlerLibraryRegistry, IExtensionChangeHandler {

	private BundleContext bundleContext;

	private Map<String, ITemplateHandlerLibrary> templateHandlerLibraries;

	/**
	 * Constructs a new {@code TemplateHandlerLibraryExtensionChangeHandler}
	 * instance.
	 */
	public TemplateHandlerLibraryExtensionChangeHandler(
			BundleContext bundleContext) {
		super();
		this.bundleContext = Assertions.mustNotBeNull("bundleContext",
				bundleContext);
		this.templateHandlerLibraries = new ConcurrentHashMap<String, ITemplateHandlerLibrary>();
	}

	public ITemplateHandlerLibrary templateHandlerLibraryForId(String identifier) {
		return this.templateHandlerLibraries.get(identifier);
	}

	public void addExtension(IExtensionTracker extensionTracker,
			IExtension extension) {
		for (IConfigurationElement configurationElement : extension
				.getConfigurationElements()) {
			String id = configurationElement.getAttribute("id");
			ITemplateHandlerLibrary templateHandlerLibrary = new TemplateHandlerLibraryDelegate(
					this.bundleContext, configurationElement, this);
			extensionTracker.registerObject(extension, id,
					IExtensionTracker.REF_STRONG);
			this.templateHandlerLibraries.put(id, templateHandlerLibrary);
		}
	}

	public void removeExtension(IExtension extension, Object[] createdExtensions) {
		for (Object id : createdExtensions) {
			this.templateHandlerLibraries.remove(id);
		}
	}

}
