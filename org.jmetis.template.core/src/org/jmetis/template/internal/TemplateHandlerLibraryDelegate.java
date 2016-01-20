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
import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.template.handler.ITemplateHandler;
import org.jmetis.template.library.ITemplateHandlerLibrary;
import org.jmetis.template.library.ITemplateHandlerLibraryRegistry;
import org.jmetis.template.library.TemplateHandlerLibrary;
import org.osgi.framework.BundleContext;

/**
 * {@code TemplateHandlerLibraryDelegate}
 * 
 * @author aerlach
 */
class TemplateHandlerLibraryDelegate extends TemplateHandlerLibrary {

	private BundleContext bundleContext;

	private IConfigurationElement configurationElement;

	private ITemplateHandlerLibraryRegistry templateHandlerLibraryRegistry;

	/**
	 * Constructs a new {@code TemplateHandlerLibraryDelegate} instance.
	 */
	public TemplateHandlerLibraryDelegate(BundleContext bundleContext,
			IConfigurationElement configurationElement,
			ITemplateHandlerLibraryRegistry templateHandlerLibraryRegistry) {
		super();
		this.bundleContext = Assertions.mustNotBeNull("bundleContext",
				bundleContext);
		this.configurationElement = Assertions.mustNotBeNull(
				"configurationElement", configurationElement);
		this.templateHandlerLibraryRegistry = Assertions.mustNotBeNull(
				"templateHandlerLibraryRegistry",
				templateHandlerLibraryRegistry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.template.handler.TemplateHandlerLibrary#
	 * createParentTemplateHandlerLibrary()
	 */
	@Override
	protected ITemplateHandlerLibrary createParentTemplateHandlerLibrary() {
		String parentId = this.configurationElement.getAttribute("parentId");
		if (parentId != null) {
			return this.templateHandlerLibraryRegistry
					.templateHandlerLibraryForId(parentId);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.template.handler.TemplateHandlerLibrary#
	 * createTemplateHandlerNamespaces()
	 */
	@Override
	protected Map<String, Map<String, ITemplateHandler>> createTemplateHandlerNamespaces() {
		IConfigurationElement[] templateHandlerChildren = this.configurationElement
				.getChildren("templateHandler"); //$NON-NLS-1$
		Map<String, Map<String, ITemplateHandler>> templateHandlerNamespaces = new ConcurrentHashMap<String, Map<String, ITemplateHandler>>(
				templateHandlerChildren.length * 2);
		for (IConfigurationElement templateHandlerChild : templateHandlerChildren) {
			String namespace = templateHandlerChild.getAttribute("uri"); //$NON-NLS-1$
			String localName = templateHandlerChild.getAttribute("name"); //$NON-NLS-1$
			ITemplateHandler templateHandler = new TemplateHandlerDelegate(
					this.bundleContext, templateHandlerChild);
			Map<String, ITemplateHandler> templateHandlers = templateHandlerNamespaces
					.get(namespace);
			if (templateHandlers == null) {
				templateHandlers = new ConcurrentHashMap<String, ITemplateHandler>();
				templateHandlerNamespaces.put(namespace, templateHandlers);
			}
			templateHandlers.put(localName, templateHandler);
		}
		return templateHandlerNamespaces;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.template.handler.TemplateHandlerLibrary#
	 * createDefaultTemplateHandler()
	 */
	@Override
	protected ITemplateHandler createDefaultTemplateHandler() {
		IConfigurationElement[] defaultTemplateHandlerChildren = this.configurationElement
				.getChildren("defaultTemplateHandler"); //$NON-NLS-1$
		if (defaultTemplateHandlerChildren.length > 0) {
			return new TemplateHandlerDelegate(this.bundleContext,
					defaultTemplateHandlerChildren[0]);
		}
		return null;
	}

}
