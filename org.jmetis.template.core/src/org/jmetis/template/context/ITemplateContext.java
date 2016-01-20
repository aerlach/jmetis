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

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import org.jmetis.kernel.context.IEvaluationContext;
import org.jmetis.template.handler.ITemplateHandler;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code ITemplateContext}
 * 
 * @author aerlach
 */
public interface ITemplateContext extends IEvaluationContext {

	ELContext getELContext();

	ITemplateContext enterChildContext(Object ownerToken);

	ITemplateContext exitChildContext(Object ownerToken);

	ExpressionFactory getExpressionFactory();

	ITemplateHandler getTemplateHandler(ITemplateNode templateNode);

	String getLocalizedString(String key);

}
