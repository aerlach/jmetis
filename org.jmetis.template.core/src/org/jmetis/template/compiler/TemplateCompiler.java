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
package org.jmetis.template.compiler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.jmetis.resource.IResourceLocator;
import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.model.TemplateNode;

/**
 * {@code TemplateCompiler}
 * 
 * @author aerlach
 */
public abstract class TemplateCompiler implements ITemplateCompiler {

	private IResourceLocator resourceLocator;

	/**
	 * Constructs a new {@code TemplateCompiler} instance with the given
	 * {@link IResourceLocator resourceLocator}.
	 * 
	 * @param resourceLocator
	 */
	public TemplateCompiler(IResourceLocator resourceLocator) {
		super();
		this.resourceLocator = resourceLocator;
	}

	/**
	 * Constructs a new {@code TemplateCompiler} instance with the default
	 * {@link IResourceLocator}.
	 */
	public TemplateCompiler() {
		this(IResourceLocator.DEFAULT_INSTANCE);
	}

	protected URL resolveTemplate(String templateName) {
		try {
			return new URL(templateName);
		} catch (MalformedURLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		try {
			URL templateURL = this.resourceLocator.resolveResource(templateName);
			if (templateURL == null) {
				// TODO throw resourceNotFound(definitionURI);
			}
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		// TODO use assertion
		throw new NullPointerException(templateName);
	}

	protected TemplateNode createTemplateNodeIn(String templateURI,
			int lineNumber, int columnNumber, String namespace,
			String localName, Map<String, String> attributes,
			ITemplateContext templateContext) {
		return new TemplateNode(templateURI, lineNumber, columnNumber,
				namespace, localName, attributes);
	}

}
