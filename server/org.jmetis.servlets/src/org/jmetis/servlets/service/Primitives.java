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
package org.jmetis.servlets.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Convenience methods for working with the DOM API, in particular for working
 * with DOM Nodes and DOM Elements.
 * 
 * @author era
 * @see org.w3c.dom.Node
 * @see org.w3c.dom.Element
 */
public abstract class Primitives {

	private static NodeList EMPTY_NODE_LIST;

	/**
	 * {@code Primitives} should not be instantiated. The constructor is
	 * protected to allow sub-classing.
	 */
	protected Primitives() {
		super();
	}

	public static NodeList childNodesOf(Element element) {
		if (element == null) {
			if (Primitives.EMPTY_NODE_LIST == null) {
				Primitives.EMPTY_NODE_LIST = new EmptyNodeList();
			}
			return Primitives.EMPTY_NODE_LIST;
		}
		return element.getChildNodes();
	}

	/**
	 * Namespace-aware equals comparison. Returns <code>true</code> if either
	 * {@link Node#getLocalName} or {@link Node#getNodeName} equals
	 * <code>desiredName</code>, otherwise returns <code>false</code>.
	 */
	public static boolean nodeNameEquals(Node node, String desiredName) {
		return desiredName.equals(node.getNodeName())
				|| desiredName.equals(node.getLocalName());
	}

	/**
	 * Adds the {@code element} to the {@code collection}. If the specified
	 * {@code collection} is {@code null}, or is same as
	 * {@link Collections#emptyList()}, then a new {@link ArrayList} is created.
	 * 
	 * @param element
	 *            the element to add.
	 * @param collection
	 *            the collection to add to or null or
	 *            {@link Collections#emptyList()} if none yet created.
	 * @return the collection created or added to.
	 */
	public static <E> List<E> addElementTo(E element, List<E> collection) {
		if (collection == null || collection == Collections.EMPTY_LIST) {
			collection = new ArrayList<E>();
		}
		collection.add(element);
		return collection;
	}

	/**
	 * Retrieves all child elements of the given {@code element} that matches
	 * the given {@code elementName}. Only look at the direct child level of the
	 * given element; do not go into further depth (in contrast to the DOM API's
	 * {@link Element#getElementsByTagName} method).
	 * 
	 * @param element
	 *            the DOM element to analyze
	 * @param elementName
	 *            the child element name to look for
	 * @return a List of child {@link Element} instances
	 * 
	 * @see Element#getElementsByTagName
	 */
	public static List<Element> allChildElementsNamed(Element element,
			String elementName) {
		NodeList childNodes = Primitives.childNodesOf(element);
		List<Element> childElements = Collections.emptyList();
		for (int i = 0, n = childNodes.getLength(); i < n; i++) {
			Node node = childNodes.item(i);
			if (node instanceof Element
					&& Primitives.nodeNameEquals(node, elementName)) {
				childElements = Primitives.addElementTo((Element) node,
						childElements);
			}
		}
		return childElements;
	}

	/**
	 * Utility method that returns the first child element identified by its
	 * name.
	 * 
	 * @param element
	 *            the DOM element to analyze
	 * @param elementName
	 *            the child element name to look for
	 * @return the <code>org.w3c.dom.Element</code> instance, or
	 *         <code>null</code> if none found
	 */
	public static Element firstChildElementNamed(Element element,
			String elementName) {
		NodeList childNodes = Primitives.childNodesOf(element);
		for (int i = 0, n = childNodes.getLength(); i < n; i++) {
			Node node = childNodes.item(i);
			if (node instanceof Element
					&& Primitives.nodeNameEquals(node, elementName)) {
				return (Element) node;
			}
		}
		return null;
	}

	/**
	 * Extract the text value from the given DOM element, ignoring XML comments.
	 * <p>
	 * Appends all CharacterData nodes and EntityReference nodes into a single
	 * String value, excluding Comment nodes.
	 * 
	 * @see CharacterData
	 * @see EntityReference
	 * @see Comment
	 */
	public static String textValueOf(Element element) {
		StringBuilder textValue = new StringBuilder();
		NodeList childNodes = element.getChildNodes();
		for (int i = 0, n = childNodes.getLength(); i < n; i++) {
			Node item = childNodes.item(i);
			if (item instanceof CharacterData && !(item instanceof Comment)
					|| item instanceof EntityReference) {
				textValue.append(item.getNodeValue());
			}
		}
		return textValue.toString().trim();
	}

	/**
	 * Utility method that returns the first child element value identified by
	 * its name.
	 * 
	 * @param element
	 *            the DOM element to analyze
	 * @param elementName
	 *            the child element name to look for
	 * @return the extracted text value, or <code>null</code> if no child
	 *         element found
	 */
	public static String textValueOfFirstChildElementNamed(Element element,
			String elementName) {
		Element child = Primitives.firstChildElementNamed(element, elementName);
		if (child != null) {
			return Primitives.textValueOf(child);
		}
		return null;
	}

}
