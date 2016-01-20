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
package org.jmetis.template.library;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jmetis.template.handler.ITemplateHandler;
import org.jmetis.template.handler.TracingTemplateHandler;

/**
 * {@code TemplateHandlerLibrary}
 * 
 * @author aerlach
 */
public class TemplateHandlerLibrary implements ITemplateHandlerLibrary {

	private ITemplateHandlerLibrary parentTemplateHandlerLibrary;

	private Map<String, Map<String, ITemplateHandler>> templateHandlerNamespaces;

	private ITemplateHandler defaultTemplateHandler;

	/**
	 * Constructs a new {@code TemplateHandlerLibrary} instance.
	 */
	public TemplateHandlerLibrary() {
		super();
	}

	protected ITemplateHandlerLibrary createParentTemplateHandlerLibrary() {
		return null;
	}

	protected ITemplateHandlerLibrary parentTemplateHandlerLibrary() {
		if (this.parentTemplateHandlerLibrary == null) {
			this.parentTemplateHandlerLibrary = this
					.createParentTemplateHandlerLibrary();
		}
		return this.parentTemplateHandlerLibrary;
	}

	protected Map<String, Map<String, ITemplateHandler>> createTemplateHandlerNamespaces() {
		return new ConcurrentHashMap<String, Map<String, ITemplateHandler>>();
	}

	protected Map<String, Map<String, ITemplateHandler>> templateHandlerNamespaces() {
		if (this.templateHandlerNamespaces == null) {
			this.templateHandlerNamespaces = this
					.createTemplateHandlerNamespaces();

		}
		return this.templateHandlerNamespaces;
	}

	protected void addTemplateHandler(String namespace, String localName,
			ITemplateHandler templateHandler) {
		Map<String, ITemplateHandler> templateHandlers;
		if (this.templateHandlerNamespaces != null) {
			templateHandlers = this.templateHandlerNamespaces.get(namespace);
		} else {
			this.templateHandlerNamespaces = new ConcurrentHashMap<String, Map<String, ITemplateHandler>>();
			templateHandlers = null;
		}
		if (templateHandlers == null) {
			templateHandlers = new ConcurrentHashMap<String, ITemplateHandler>();
			this.templateHandlerNamespaces.put(namespace, templateHandlers);
		}
		templateHandlers.put(localName, templateHandler);
	}

	protected void removePartBuilderType(String namespace, String localName) {
		if (this.templateHandlerNamespaces != null) {
			Map<String, ITemplateHandler> templateHandlers = this.templateHandlerNamespaces
					.get(namespace);
			if (templateHandlers != null) {
				templateHandlers.remove(localName);
			}
		}
	}

	protected ITemplateHandler createDefaultTemplateHandler() {
		return new TracingTemplateHandler();
	}

	protected ITemplateHandler defaultTemplateHandler() {
		if (this.defaultTemplateHandler == null) {
			this.defaultTemplateHandler = this.createDefaultTemplateHandler();
		}
		return this.defaultTemplateHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.handler.ITemplateHandlerLibrary#createTemplateHandler
	 * (java.lang.String, java.lang.String)
	 */
	public ITemplateHandler createTemplateHandler(String namespace,
			String localName) {
		Map<String, Map<String, ITemplateHandler>> templateHandlerNamespaces = this
				.templateHandlerNamespaces();
		if (templateHandlerNamespaces != null) {
			Map<String, ITemplateHandler> templateHandlers = templateHandlerNamespaces
					.get(namespace);
			if (templateHandlers != null) {
				ITemplateHandler templateHandler = templateHandlers
						.get(localName);
				if (templateHandler != null) {
					return templateHandler;
				}
			}
		}
		ITemplateHandlerLibrary parentTemplateHandlerLibrary = this
				.parentTemplateHandlerLibrary();
		if (parentTemplateHandlerLibrary != null) {
			ITemplateHandler templateHandler = parentTemplateHandlerLibrary
					.createTemplateHandler(namespace, localName);
			if (templateHandler != null) {
				return templateHandler;
			}
		}
		return this.defaultTemplateHandler();
	}

}
