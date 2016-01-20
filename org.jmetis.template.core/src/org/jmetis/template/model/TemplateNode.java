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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jmetis.template.context.ITemplateContext;

/**
 * {@code TemplateNode}
 * 
 * @author aerlach
 */
public class TemplateNode implements ITemplateNode {

	private String templateURI;

	private int lineNumber;

	private int columnNumber;

	private String namespace;

	private String localName;

	private Map<String, String> attributes;

	private TemplateNode parentNode;

	private List<TemplateNode> childNodes;

	private ITemplateNode predecessorNode;

	private ITemplateNode successorNode;

	public TemplateNode(String templateURI, int lineNumber, int columnNumber,
			String namespace, String localName, Map<String, String> attributes) {
		super();
		this.templateURI = templateURI;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		this.namespace = namespace;
		this.localName = localName;
		this.attributes = attributes;
	}

	public String getTemplateURI() {
		return this.templateURI;
	}

	public int getLineNumber() {
		return this.lineNumber;
	}

	public int getColumnNumber() {
		return this.columnNumber;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public String getLocalName() {
		return this.localName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateNode#getAttributeNames()
	 */
	public String[] getAttributeNames() {
		return this.attributes.keySet().toArray(
				new String[this.attributes.size()]);
	}

	public String getAttribute(String name) {
		return this.attributes.get(name);
	}

	/**
	 * Returns the parent of the receiver. If the receiver has no parent, this
	 * returns {@code null}.
	 * 
	 * @return the parent of the receiver. If the receiver has no parent, this
	 *         returns {@code null}
	 */
	public TemplateNode getParentNode() {
		return this.parentNode;
	}

	/**
	 * Returns all children of the receiver.
	 * 
	 * @return all children of the receiver
	 */
	public ITemplateNode[] getChildNodes() {
		if (this.childNodes == null) {
			return new ITemplateNode[0];
		}
		return this.childNodes
				.toArray(new TemplateNode[this.childNodes.size()]);
	}

	public void addChildNode(TemplateNode childNode) {
		childNode.parentNode = this;
		if (this.childNodes == null) {
			this.childNodes = new ArrayList<TemplateNode>();
		} else {
			TemplateNode previousNode = this.childNodes.get(this.childNodes
					.size() - 1);
			childNode.predecessorNode = previousNode;
			previousNode.successorNode = childNode;
		}
		this.childNodes.add(childNode);
	}

	/**
	 * Returns the {@link TemplateNode} immediately preceding the receiver. If
	 * there is no such {@link TemplateNode}, this returns {@code null}.
	 * 
	 * @return the {@link TemplateNode} immediately preceding the receiver. If
	 *         there is no such {@link TemplateNode}, this returns {@code null}
	 */
	public ITemplateNode getPredecessorNode() {
		return this.predecessorNode;
	}

	/**
	 * Returns the {@link TemplateNode} immediately following the receiver. If
	 * there is no such {@link TemplateNode}, this returns {@code null}.
	 * 
	 * @return the {@link TemplateNode} immediately following the receiver. If
	 *         there is no such {@link TemplateNode}, this returns {@code null}
	 */
	public ITemplateNode getSuccessorNode() {
		return this.successorNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateNode#performIn(org.jmetis.template.
	 * core.ITemplateContext)
	 */
	public void performIn(ITemplateContext templateContext) {
		templateContext.getTemplateHandler(this).performIn(this,
				templateContext);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder().append("<").append(
				this.localName).append("> ").append(this.templateURI).append(
				'[').append(this.lineNumber).append(", ").append(
				this.columnNumber).append(']');
		if (this.childNodes != null) {
			for (ITemplateNode currentNode : this.childNodes) {
				stringBuilder.append('\n').append('\t').append(
						currentNode.toString());
			}
		}
		stringBuilder.append('\n');
		return stringBuilder.toString();
	}

}
