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
package org.jmetis.template.handler;

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code TemplateHandler}
 * 
 * @author aerlach
 */
public class TemplateHandlerDecorator implements ITemplateHandler {

	private ITemplateHandler templateHandler;

	/**
	 * Constructs a new {@code TemplateHandler} instance.
	 */
	public TemplateHandlerDecorator(ITemplateHandler templateHandler) {
		super();
		this.templateHandler = Assertions.mustNotBeNull("templateHandler",
				templateHandler);
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
		this.templateHandler.performIn(templateNode, templateContext);
	}

}
