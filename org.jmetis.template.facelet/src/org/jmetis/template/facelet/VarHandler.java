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

import javax.el.VariableMapper;

import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.handler.TemplateHandler;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code VarHandler}
 * 
 * @author aerlach
 */
public class VarHandler extends TemplateHandler {

	/**
	 * Constructs a new {@code VarHandler} instance.
	 */
	public VarHandler() {
		super();
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
		String varName = this.getRequiredAttributeIn(templateNode, "name",
				templateContext);
		String varValue = this.getRequiredAttributeIn(templateNode, "value",
				templateContext);
		VariableMapper variableMapper = templateContext.getELContext()
				.getVariableMapper();
		if (variableMapper == null) {
			throw new NullPointerException("VariableMapper must not be null");
		}
		variableMapper
				.setVariable(varName, templateContext.getExpressionFactory()
						.createValueExpression(templateContext.getELContext(),
								varValue, Object.class));
	}

}
