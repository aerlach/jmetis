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
package org.jmetis.template.facelet;

import org.jmetis.template.compiler.ITemplateCompiler;
import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.handler.TemplateHandler;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code IncludeHandler}
 * 
 * @author aerlach
 */
public class IncludeHandler extends TemplateHandler {

	/**
	 * Constructs a new {@code IncludeHandler} instance.
	 */
	public IncludeHandler() {
		super();
	}

	protected void includeTemplateIn(String templateName,
			ITemplateContext templateContext) {
		// TODO
		ITemplateCompiler templateCompiler = null;
		ITemplateNode templateNode = templateCompiler.compileTemplateIn(
				templateName, templateContext);
		templateNode.performIn(templateContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.handler.TemplateHandler#performIn(org.jmetis.template
	 * .model.ITemplateNode, org.jmetis.template.context.ITemplateContext)
	 */
	@Override
	public void performIn(ITemplateNode templateNode,
			ITemplateContext templateContext) {
		String templateName = this.getRequiredAttributeIn(templateNode, "src",
				templateContext);
		templateContext = templateContext.enterChildContext(this);
		try {
			super.performIn(templateNode, templateContext);
			this.includeTemplateIn(templateName, templateContext);
		} finally {
			templateContext.exitChildContext(this);
		}
	}

}
