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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.model.ITemplateNode;
import org.jmetis.template.model.TemplateNode;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * {@code SAXTemplateHandler}
 * 
 * @author aerlach
 */
class SAXTemplateHandler extends DefaultHandler {

	private SAXTemplateCompiler templateCompiler;

	private ITemplateContext templateContext;

	private Locator documentLocator;

	private TemplateNode currentNode;

	/**
	 * Constructs a new {@code SAXTemplateHandler} instance.
	 */
	public SAXTemplateHandler(SAXTemplateCompiler templateCompiler,
			ITemplateContext templateContext) {
		super();
		this.templateCompiler = templateCompiler;
		this.templateContext = templateContext;
	}

	@Override
	public void setDocumentLocator(Locator documentLocator) {
		this.documentLocator = documentLocator;
	}

	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException {
		String dtd = "default.dtd";
		URL url = Thread.currentThread().getContextClassLoader().getResource(
				dtd);
		return new InputSource(url.toString());
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		if (this.documentLocator != null) {
			throw new SAXException("Error Traced[line: "
					+ this.documentLocator.getLineNumber() + "] "
					+ exception.getMessage());
		}
		throw exception;
	}

	protected Map<String, String> createAttributesIn(Attributes attributeList,
			ITemplateContext templateContext) {
		int attributeCount = attributeList.getLength();
		Map<String, String> attributeMap = new HashMap<String, String>(
				attributeCount * 2);
		for (int i = 0; i < attributeCount; i++) {
			String localName = attributeList.getLocalName(i);
			if (!"schemaLocation".equals(localName)) { //$NON-NLS-1$
				attributeMap.put(localName, attributeList.getValue(i));
			}
		}
		return attributeMap;
	}

	protected TemplateNode createTemplateNode(String namespace,
			String localName, Attributes attributes) {
		return this.templateCompiler.createTemplateNodeIn(this.documentLocator
				.getSystemId(), this.documentLocator.getLineNumber(),
				this.documentLocator.getColumnNumber(), namespace, localName,
				this.createAttributesIn(attributes, this.templateContext),
				this.templateContext);
	}

	@Override
	public void startElement(String namespace, String localName, String qName,
			Attributes attributes) throws SAXException {
		TemplateNode templateNode = this.createTemplateNode(namespace,
				localName, attributes);
		if (this.currentNode != null) {
			this.currentNode.addChildNode(templateNode);
		}
		this.currentNode = templateNode;
	}

	@Override
	public void endElement(String namespace, String localName, String qName)
			throws SAXException {
		TemplateNode parentNode = this.currentNode.getParentNode();
		if (parentNode != null) {
			this.currentNode = parentNode;
		}
	}

	/**
	 * @return the currentNode
	 */
	public ITemplateNode getCurrentNode() {
		return this.currentNode;
	}

}
