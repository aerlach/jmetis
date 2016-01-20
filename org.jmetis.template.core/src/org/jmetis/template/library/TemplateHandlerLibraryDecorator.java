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

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.template.handler.ITemplateHandler;

/**
 * {@code TemplateHandlerLibraryDecorator}
 * 
 * @author aerlach
 */
public class TemplateHandlerLibraryDecorator implements ITemplateHandlerLibrary {

	private ITemplateHandlerLibrary templateHandlerLibrary;

	/**
	 * Constructs a new {@code TemplateHandlerLibraryDecorator} instance.
	 */
	public TemplateHandlerLibraryDecorator(
			ITemplateHandlerLibrary templateHandlerLibrary) {
		super();
		this.templateHandlerLibrary = Assertions.mustNotBeNull(
				"templateHandlerLibrary", templateHandlerLibrary);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateHandlerLibrary#createTemplateHandler
	 * (java.lang.String, java.lang.String)
	 */
	public ITemplateHandler createTemplateHandler(String namespace,
			String localName) {
		return this.templateHandlerLibrary.createTemplateHandler(namespace,
				localName);
	}

}
