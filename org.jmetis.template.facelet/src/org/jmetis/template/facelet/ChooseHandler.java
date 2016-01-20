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

import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.handler.TemplateHandler;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code ChooseHandler}
 * 
 * @author aerlach
 */
public class ChooseHandler extends TemplateHandler {

	/**
	 * Constructs a new {@code ChooseHandler} instance.
	 */
	public ChooseHandler() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.handler.TemplateHandler#buildComponentIn(org.jmetis
	 * .template.model.ITemplateNode,
	 * org.jmetis.template.context.ITemplateContext)
	 */
	@Override
	protected void buildComponentIn(ITemplateNode templateNode,
			ITemplateContext templateContext) {
		for (ITemplateNode childNode : templateNode.getChildNodes()) {
			if (this.getBooleanAttributeIn(childNode, "test", templateContext)) {
				childNode.performIn(templateContext);
			}
		}
	}

}
