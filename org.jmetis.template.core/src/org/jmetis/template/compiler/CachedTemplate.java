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
import org.jmetis.template.model.TemplateNodeDecorator;

/**
 * Default {@link ITemplateNode} implementation.
 * 
 * @author aerlach
 */
class CachedTemplate extends TemplateNodeDecorator {

	private ITemplateCompiler templateCompiler;

	private String templateName;

	private ITemplateContext templateContext;

	private ITemplateNode templateNode;

	private long creationTime;

	/**
	 * Constructs a new {@code CachedTemplate} instance.
	 * 
	 * @param templateCompiler
	 * @param templateURL
	 */
	public CachedTemplate(ITemplateCompiler templateCompiler,
			String templateName, ITemplateContext templateContext) {
		super();
		this.templateCompiler = templateCompiler;
		this.templateName = templateName;
		this.templateContext = templateContext;
	}

	@Override
	protected ITemplateNode getTemplateNode() {
		if (this.templateNode == null) {
			this.creationTime = System.currentTimeMillis();
			this.templateNode = this.templateCompiler.compileTemplateIn(
					this.templateName, this.templateContext);
		}
		return this.templateNode;
	}

	/**
	 * Returns the time in milliseconds when the receiver was created.
	 * 
	 * @return the time in milliseconds when the receiver was created
	 */
	public long getCreationTime() {
		return this.creationTime;
	}

}
