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
package org.jmetis.template.model;

import org.jmetis.template.context.ITemplateContext;

/**
 * {@code ITemplateNode} ...
 * 
 * @author aerlach
 */
public interface ITemplateNode {

	/**
	 * @return
	 */
	String getTemplateURI();

	/**
	 * @return
	 */
	int getLineNumber();

	/**
	 * @return
	 */
	int getColumnNumber();

	/**
	 * 
	 * @return
	 */
	String getNamespace();

	/**
	 * 
	 * @return
	 */
	String getLocalName();

	String[] getAttributeNames();

	/**
	 * 
	 * @param name
	 * @return
	 */
	String getAttribute(String name);

	/**
	 * Returns the parent of the receiver. If the receiver has no parent, this
	 * returns {@code null}.
	 * 
	 * @return the parent of the receiver. If the receiver has no parent, this
	 *         returns {@code null}
	 */
	ITemplateNode getParentNode();

	/**
	 * Returns all children of the receiver.
	 * 
	 * @return all children of the receiver
	 */
	ITemplateNode[] getChildNodes();

	/**
	 * Returns the {@link TemplateNode} immediately preceding the receiver. If
	 * there is no such {@link TemplateNode}, this returns {@code null}.
	 * 
	 * @return the {@link TemplateNode} immediately preceding the receiver. If
	 *         there is no such {@link TemplateNode}, this returns {@code null}
	 */
	ITemplateNode getPredecessorNode();

	/**
	 * Returns the {@link TemplateNode} immediately following the receiver. If
	 * there is no such {@link TemplateNode}, this returns {@code null}.
	 * 
	 * @return the {@link TemplateNode} immediately following the receiver. If
	 *         there is no such {@link TemplateNode}, this returns {@code null}
	 */
	ITemplateNode getSuccessorNode();

	/**
	 * Renders a particular user interface component.
	 * 
	 * @param templateContext
	 *            the current {@link ITemplateContext} instance for this
	 *            execution
	 */
	void performIn(ITemplateContext templateContext);

}
