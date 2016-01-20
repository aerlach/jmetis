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
package org.jmetis.template.context;

import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.el.VariableMapper;

import org.jmetis.kernel.assertion.Assertions;

/**
 * {@code VariableMapperAdapter}
 * 
 * @author aerlach
 */
class VariableMapperAdapter extends VariableMapper {

	private TemplateContext templateContext;

	private VariableMapper parentVariableMapper;

	private Map<String, ValueExpression> variables;

	/**
	 * Constructs a new {@code VariableMapperAdapter} instance.
	 */
	VariableMapperAdapter(TemplateContext templateContext,
			VariableMapper parentVariableMapper) {
		super();
		this.templateContext = Assertions.mustNotBeNull("templateContext",
				templateContext);
		this.parentVariableMapper = Assertions.mustNotBeNull(
				"parentVariableMapper", parentVariableMapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.VariableMapper#resolveVariable(java.lang.String)
	 */
	@Override
	public ValueExpression resolveVariable(String variable) {
		ValueExpression valueExpression = null;
		if (this.variables != null) {
			valueExpression = this.variables.get(variable);
		}
		if (valueExpression == null) {
			Object value = this.templateContext.getAttribute(variable);
			if (value != null) {
				valueExpression = this.templateContext.getExpressionFactory()
						.createValueExpression(value, Object.class);
				this.variables.put(variable, valueExpression);
			} else {
				valueExpression = this.parentVariableMapper
						.resolveVariable(variable);
			}
		}
		return valueExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.VariableMapper#setVariable(java.lang.String,
	 * javax.el.ValueExpression)
	 */
	@Override
	public ValueExpression setVariable(String variable,
			ValueExpression expression) {
		if (this.variables == null) {
			this.variables = new HashMap<String, ValueExpression>();
		}
		ValueExpression oldExpression = this.variables
				.put(variable, expression);
		if (expression != null) {
			this.templateContext.setAttribute(variable, expression
					.getValue(this.templateContext));
		} else if (oldExpression != null) {
			this.templateContext.setAttribute(variable, null);
		}
		return oldExpression;
	}
}
