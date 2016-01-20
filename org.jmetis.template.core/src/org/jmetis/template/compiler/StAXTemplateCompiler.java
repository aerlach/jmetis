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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.model.ITemplateNode;
import org.jmetis.template.model.TemplateNode;

/**
 * {@code StAXTemplateCompiler} is a {@link ITemplateCompiler} implementation
 * that uses the pull-based Streaming API for XML.
 * 
 * @author aerlach
 */
public class StAXTemplateCompiler extends TemplateCompiler {

	private XMLInputFactory parserFactory;

	/**
	 * Constructs a new {@code StAXTemplateCompiler} instance.
	 * 
	 * @param parserFactory
	 * @param partBuilderFactory
	 */
	public StAXTemplateCompiler() {
		super();
		this.parserFactory = this.createParserFactory();
	}

	protected XMLInputFactory createParserFactory() {
		return XMLInputFactory.newInstance();
	}

	protected Map<String, String> createAttributesIn(
			XMLStreamReader streamReader, ITemplateContext templateContext) {
		int attributeCount = streamReader.getAttributeCount();
		Map<String, String> attributes = new HashMap<String, String>(
				attributeCount * 2);
		for (int i = 0; i < attributeCount; i++) {
			String localName = streamReader.getAttributeLocalName(i);
			if (!"schemaLocation".equals(localName)) { //$NON-NLS-1$
				attributes.put(localName, streamReader.getAttributeValue(i));
			}
		}
		return attributes;
	}

	protected TemplateNode createTemplateNode(XMLStreamReader streamReader,
			ITemplateContext templateContext) {
		Location location = streamReader.getLocation();
		return this.createTemplateNodeIn(location.getSystemId(), location
				.getLineNumber(), location.getColumnNumber(), streamReader
				.getNamespaceURI(), streamReader.getLocalName(), this
				.createAttributesIn(streamReader, templateContext),
				templateContext);

	}

	public ITemplateNode compileTemplateIn(String templateName,
			ITemplateContext templateContext) {
		try {
			TemplateNode currentNode = null;
			InputStream inputStream = this.resolveTemplate(templateName)
					.openStream();
			try {
				XMLStreamReader streamReader = this.parserFactory
						.createXMLStreamReader(templateName, inputStream);
				while (streamReader.hasNext()) {
					switch (streamReader.next()) {
					case XMLStreamConstants.START_ELEMENT:
						TemplateNode templateNode = this.createTemplateNode(
								streamReader, templateContext);
						if (currentNode != null) {
							currentNode.addChildNode(templateNode);
						}
						currentNode = templateNode;
						break;
					case XMLStreamConstants.END_ELEMENT:
						TemplateNode parentNode = currentNode.getParentNode();
						if (parentNode != null) {
							currentNode = parentNode;
						}
						break;
					}
				}

			} finally {
				inputStream.close();
			}
			return currentNode;
		} catch (IOException ex) {
			throw new RuntimeException("Error Parsing " + templateName + ": "
					+ ex.getMessage(), ex.getCause());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error Parsing " + templateName + ": "
					+ ex.getMessage(), ex.getCause());
		}
	}

}
