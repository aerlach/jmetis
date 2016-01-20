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

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.template.context.ITemplateContext;

/**
 * {@code TemplateNodeDecorator}
 * 
 * @author aerlach
 */
public class TemplateNodeDecorator implements ITemplateNode {

	private ITemplateNode templateNode;

	/**
	 * Constructs a new {@code TemplateNodeDecorator} instance.
	 */
	protected TemplateNodeDecorator() {
		super();
	}

	/**
	 * Constructs a new {@code TemplateNodeDecorator} instance.
	 * 
	 * @param {@link ITemplateNode} that handles the behavior of the template
	 *        node
	 */
	public TemplateNodeDecorator(ITemplateNode templateNode) {
		this();
		this.templateNode = Assertions.mustNotBeNull("templateNode",
				templateNode);
	}

	/**
	 * @return the templateNode
	 */
	protected ITemplateNode getTemplateNode() {
		return this.templateNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateNode#getTemplateURI()
	 */
	public String getTemplateURI() {
		return this.getTemplateNode().getTemplateURI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateNode#getLineNumber()
	 */
	public int getLineNumber() {
		return this.getTemplateNode().getLineNumber();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateNode#getColumnNumber()
	 */
	public int getColumnNumber() {
		return this.getTemplateNode().getColumnNumber();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateNode#getNamespace()
	 */
	public String getNamespace() {
		return this.getTemplateNode().getNamespace();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateNode#getLocalName()
	 */
	public String getLocalName() {
		return this.getTemplateNode().getLocalName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateNode#getAttributeNames()
	 */
	public String[] getAttributeNames() {
		return this.getTemplateNode().getAttributeNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateNode#getAttribute(java.lang.String)
	 */
	public String getAttribute(String name) {
		return this.getTemplateNode().getAttribute(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateNode#getParentNode()
	 */
	public ITemplateNode getParentNode() {
		return this.getTemplateNode().getParentNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateNode#getChildNodes()
	 */
	public ITemplateNode[] getChildNodes() {
		return this.getTemplateNode().getChildNodes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateNode#getPredecessorNode()
	 */
	public ITemplateNode getPredecessorNode() {
		return this.getTemplateNode().getPredecessorNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateNode#getSuccessorNode()
	 */
	public ITemplateNode getSuccessorNode() {
		return this.getTemplateNode().getSuccessorNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateNode#performIn(org.jmetis.template.
	 * core.ITemplateContext)
	 */
	public void performIn(ITemplateContext templateContext) {
		// TODO TEMPLATE HANDLER performIn(TemplateNode, TemplateContext)
		// template handler calls
		// - prepare
		// - perform on children
		// - complete
		this.getTemplateNode().performIn(templateContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {
		return this.getTemplateNode().equals(otherObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getTemplateNode().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getTemplateNode().toString();
	}

}
