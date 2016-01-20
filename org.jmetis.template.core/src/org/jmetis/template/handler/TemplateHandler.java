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

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code TemplateHandler}
 * 
 * @author aerlach
 */
public class TemplateHandler implements ITemplateHandler {

	/**
	 * Constructs a new {@code TemplateHandler} instance.
	 */
	public TemplateHandler() {
		super();
	}

	protected String getAttributeIn(ITemplateNode templateNode,
			String attributeName, ITemplateContext templateContext) {
		return templateNode.getAttribute(attributeName);
	}

	protected boolean getBooleanAttributeIn(ITemplateNode templateNode,
			String attributeName, ITemplateContext templateContext) {
		return Boolean.valueOf(
				this.getAttributeIn(templateNode, attributeName,
						templateContext)).booleanValue();
	}

	protected int getIntAttributeIn(ITemplateNode templateNode,
			String attributeName, ITemplateContext templateContext) {
		String attributeValue = this.getAttributeIn(templateNode,
				attributeName, templateContext);
		if (attributeValue != null) {
			return Integer.parseInt(attributeValue);
		}
		return 0;
	}

	protected String getRequiredAttributeIn(ITemplateNode templateNode,
			String attributeName, ITemplateContext templateContext) {
		String attributeValue = this.getAttributeIn(templateNode,
				attributeName, templateContext);
		if (attributeValue == null) {
			// TODO
		}
		return attributeValue;
	}

	protected ValueExpression getValueExpressionIn(ITemplateNode templateNode,
			String attributeName, Class<?> expectedType,
			ITemplateContext templateContext) {
		try {
			ExpressionFactory expressionFactory = templateContext
					.getExpressionFactory();
			return expressionFactory.createValueExpression(templateContext
					.getELContext(), this.getAttributeIn(templateNode,
					attributeName, templateContext), expectedType);
		} catch (Exception ex) {
			throw new RuntimeException("Create ValueExpression failed", ex);
		}
	}

	protected MethodExpression getMethodExpressionIn(
			ITemplateNode templateNode, String attributeName,
			Class<?> declaringType, Class<?>[] parameterTypes,
			ITemplateContext templateContext) {
		try {
			ExpressionFactory expressionFactory = templateContext
					.getExpressionFactory();
			return expressionFactory.createMethodExpression(templateContext
					.getELContext(), this.getAttributeIn(templateNode,
					attributeName, templateContext), declaringType,
					parameterTypes);
		} catch (Exception ex) {
			throw new RuntimeException("Create MethodExpression failed", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateHandler#prepareComponentIn(org.jmetis
	 * .template.core.ITemplateNode,
	 * org.jmetis.template.context.ITemplateContext)
	 */
	protected void prepareComponentIn(ITemplateNode templateNode,
			ITemplateContext templateContext) {
		// TODO Auto-generated method stub

	}

	protected void buildComponentIn(ITemplateNode templateNode,
			ITemplateContext templateContext) {
		for (ITemplateNode childNode : templateNode.getChildNodes()) {
			childNode.performIn(templateContext);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateHandler#completeComponentIn(org.
	 * jmetis .template.core.ITemplateNode,
	 * org.jmetis.template.context.ITemplateContext)
	 */
	protected void completeComponentIn(ITemplateNode templateNode,
			ITemplateContext templateContext) {
		// TODO Auto-generated method stub

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
		this.prepareComponentIn(templateNode, templateContext);
		this.buildComponentIn(templateNode, templateContext);
		this.completeComponentIn(templateNode, templateContext);
	}

}
