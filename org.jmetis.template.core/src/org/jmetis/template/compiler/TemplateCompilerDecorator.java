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

import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code TemplateCompilerDecorator}
 * 
 * @author aerlach
 */
public class TemplateCompilerDecorator implements ITemplateCompiler {

	protected ITemplateCompiler templateCompiler;

	/**
	 * Constructs a new {@code TemplateCompilerDecorator} instance.
	 */
	public TemplateCompilerDecorator(ITemplateCompiler templateCompiler) {
		super();
		this.templateCompiler = templateCompiler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateCompiler#compileTemplateIn(java.net
	 * .URI, org.jmetis.template.context.ITemplateContext)
	 */
	public ITemplateNode compileTemplateIn(String templateName,
			ITemplateContext templateContext) {
		return this.templateCompiler.compileTemplateIn(templateName,
				templateContext);
	}

}
