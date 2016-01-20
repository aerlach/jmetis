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

import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code TracingTemplateHandler}
 * 
 * @author aerlach
 */
public class TracingTemplateHandler extends TemplateHandler {

	/**
	 * Constructs a new {@code TracingTemplateHandler} instance.
	 */
	public TracingTemplateHandler() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.handler.TemplateHandler#prepareComponentIn(org.jmetis
	 * .template.core.ITemplateNode,
	 * org.jmetis.template.context.ITemplateContext)
	 */
	@Override
	protected void prepareComponentIn(ITemplateNode templateNode,
			ITemplateContext templateContext) {
		System.out.print("<" + templateNode.getLocalName());
		String[] attributeNames = templateNode.getAttributeNames();
		if (attributeNames.length > 0) {
			for (String name : attributeNames) {
				System.out.print(" " + name + "=\""
						+ templateNode.getAttribute(name) + '"');
			}
		}
		System.out.println(">");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.handler.TemplateHandler#completeComponentIn(org.jmetis
	 * .template.core.ITemplateNode,
	 * org.jmetis.template.context.ITemplateContext)
	 */
	@Override
	protected void completeComponentIn(ITemplateNode templateNode,
			ITemplateContext templateContext) {
		System.out.println("</" + templateNode.getLocalName() + ">");
	}

}
