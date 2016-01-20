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
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.model.ITemplateNode;
import org.jmetis.template.model.TemplateNode;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * {@code SAXTemplateCompiler} is a {@link ITemplateCompiler} implementation
 * that uses the push-based Simple API for XML.
 * 
 * @author aerlach
 */
public class SAXTemplateCompiler extends TemplateCompiler {

	private SAXParserFactory parserFactory;

	/**
	 * Constructs a new {@code SAXTemplateCompiler} instance.
	 * 
	 * @param parserFactory
	 * @param partHandlerLibrary
	 */
	public SAXTemplateCompiler() {
		super();
		this.parserFactory = this.createParserFactory();
	}

	protected SAXParserFactory createParserFactory() {
		try {
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			parserFactory.setNamespaceAware(true);
			parserFactory.setFeature(
					"http://xml.org/sax/features/namespace-prefixes", true);
			parserFactory.setFeature("http://xml.org/sax/features/validation",
					true);
			parserFactory.setValidating(true);
			return parserFactory;
		} catch (SAXNotRecognizedException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (SAXNotSupportedException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (ParserConfigurationException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	protected TemplateNode createTemplateNodeIn(String templateURI,
			int lineNumber, int columnNumber, String namespace,
			String localName, Map<String, String> attributes,
			ITemplateContext templateContext) {
		return super
				.createTemplateNodeIn(templateURI, lineNumber, columnNumber,
						namespace, localName, attributes, templateContext);
	}

	protected SAXTemplateHandler createTemplateHandler(
			ITemplateContext templateContext) {
		return new SAXTemplateHandler(this, templateContext);
	}

	public ITemplateNode compileTemplateIn(String templateName,
			ITemplateContext templateContext) {
		try {
			SAXParser parser = this.parserFactory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			SAXTemplateHandler templateHandler = this
					.createTemplateHandler(templateContext);
			reader.setContentHandler(templateHandler);
			reader.setErrorHandler(templateHandler);
			reader.setEntityResolver(templateHandler);
			reader.parse(templateName);
			return templateHandler.getCurrentNode();
		} catch (IOException ex) {
			throw new RuntimeException("Error Parsing " + templateName + ": "
					+ ex.getMessage(), ex.getCause());
		} catch (SAXException ex) {
			throw new RuntimeException("Error Parsing " + templateName + ": "
					+ ex.getMessage(), ex.getCause());
		} catch (ParserConfigurationException ex) {
			throw new RuntimeException("Error Configuring Parser "
					+ templateName + ": " + ex.getMessage(), ex.getCause());
		}
	}

}
