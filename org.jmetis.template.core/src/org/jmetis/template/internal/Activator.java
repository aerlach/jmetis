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

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.dynamichelpers.ExtensionTracker;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.jmetis.template.compiler.ITemplateCompiler;
import org.jmetis.template.compiler.StAXTemplateCompiler;
import org.jmetis.template.compiler.TemplateCompiler;
import org.jmetis.template.library.ITemplateHandlerLibraryRegistry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

/**
 * {@code Activator}
 * 
 * @author aerlach
 */
public class Activator implements BundleActivator {

	private static final String TEMPLATE_HANDLER_LIBRARIES_EXTENSION_POINT_ID = "org.jmetis.template.templateHandlerLibraries";

	static ILog BUNDLE_LOGGER;

	private ServiceTracker extensionRegistryTracker;

	private IExtensionRegistry extensionRegistry;

	private TemplateCompiler templateCompiler;

	private IExtensionPoint templateHandlerLibrariesExtensionPoint;

	private TemplateHandlerLibraryExtensionChangeHandler templateHandlerLibraryExtensionChangeHandler;

	private IExtensionTracker extensionTracker;

	private ServiceRegistration templateHandlerLibraryRegistryRegistration;

	private ServiceRegistration templateCompilerRegistration;

	/**
	 * Constructs a new {@code Activator} instance.
	 */
	public Activator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.BUNDLE_LOGGER = Platform.getLog(bundleContext.getBundle());
		this.extensionRegistryTracker = new ServiceTracker(bundleContext,
				IExtensionRegistry.class.getName(), null);
		this.extensionRegistryTracker.open();
		this.templateCompiler = new StAXTemplateCompiler();
		do {
			this.extensionRegistry = (IExtensionRegistry) this.extensionRegistryTracker
					.getService();
		} while (this.extensionRegistry == null);
		this.templateHandlerLibrariesExtensionPoint = this.extensionRegistry
				.getExtensionPoint(Activator.TEMPLATE_HANDLER_LIBRARIES_EXTENSION_POINT_ID);
		if (this.templateHandlerLibrariesExtensionPoint != null) {
			this.extensionTracker = new ExtensionTracker(this.extensionRegistry);
			this.templateHandlerLibraryExtensionChangeHandler = new TemplateHandlerLibraryExtensionChangeHandler(
					bundleContext);
			this.extensionTracker
					.registerHandler(
							this.templateHandlerLibraryExtensionChangeHandler,
							ExtensionTracker
									.createExtensionPointFilter(this.templateHandlerLibrariesExtensionPoint));
			for (IExtension extension : this.templateHandlerLibrariesExtensionPoint
					.getExtensions()) {
				this.templateHandlerLibraryExtensionChangeHandler.addExtension(
						this.extensionTracker, extension);
			}
		}
		this.templateHandlerLibraryRegistryRegistration = bundleContext
				.registerService(ITemplateHandlerLibraryRegistry.class
						.getName(),
						this.templateHandlerLibraryExtensionChangeHandler, null);
		this.templateCompilerRegistration = bundleContext.registerService(
				ITemplateCompiler.class.getName(), this.templateCompiler, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.extensionRegistryTracker != null) {
			this.extensionRegistryTracker.close();
			this.extensionRegistryTracker = null;
		}
		if (this.templateCompilerRegistration != null) {
			this.templateCompilerRegistration.unregister();
			this.templateCompilerRegistration = null;
		}
		if (this.templateHandlerLibraryRegistryRegistration != null) {
			this.templateHandlerLibraryRegistryRegistration.unregister();
			this.templateHandlerLibraryRegistryRegistration = null;
		}
		if (this.extensionTracker != null) {
			this.extensionTracker.close();
			this.extensionTracker = null;
			if (this.templateHandlerLibrariesExtensionPoint != null) {
				for (IExtension extension : this.templateHandlerLibrariesExtensionPoint
						.getExtensions()) {
					this.templateHandlerLibraryExtensionChangeHandler
							.removeExtension(extension, this.extensionTracker
									.getObjects(extension));
				}
			}
		}

	}

}
