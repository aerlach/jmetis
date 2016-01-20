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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.handler.ITemplateHandler;
import org.jmetis.template.model.ITemplateNode;
import org.osgi.framework.BundleContext;

/**
 * {@code TemplateHandlerDelegate}
 * 
 * @author aerlach
 */
public class TemplateHandlerDelegate implements ITemplateHandler {

	private BundleContext bundleContext;

	private IConfigurationElement configurationElement;

	private ITemplateHandler templateHandler;

	/**
	 * Constructs a new {@code TemplateHandlerDelegate} instance.
	 */
	public TemplateHandlerDelegate(BundleContext bundleContext,
			IConfigurationElement configurationElement) {
		super();
		this.bundleContext = Assertions.mustNotBeNull("bundleContext",
				bundleContext);
		this.configurationElement = Assertions.mustNotBeNull(
				"configurationElement", configurationElement);
	}

	protected ITemplateHandler handleCreateExecutableExtensionFailed(
			Exception exception) {
		Activator.BUNDLE_LOGGER.log(new Status(IStatus.ERROR,
				"org.jmetis.template.core", IStatus.OK, exception
						.getLocalizedMessage(), exception));
		throw new RuntimeException(exception);
	}

	protected ITemplateHandler templateHandler() {
		if (this.templateHandler == null) {
			try {
				this.templateHandler = (ITemplateHandler) this.configurationElement
						.createExecutableExtension("class"); //$NON-NLS-1$
			} catch (Exception ex) {
				this.templateHandler = this
						.handleCreateExecutableExtensionFailed(ex);
			}
		}
		return this.templateHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateHandler#performIn(org.jmetis.template
	 * .core.ITemplateNode, org.jmetis.template.context.ITemplateContext)
	 */
	public void performIn(ITemplateNode templateNode,
			ITemplateContext templateContext) {
		this.templateHandler().performIn(templateNode, templateContext);
	}

}
