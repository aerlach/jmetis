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
package org.jmetis.template.library;

import org.jmetis.template.handler.ITemplateHandler;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code ITemplateHandlerLibrary} represents a library of {@link ITemplateNode}
 * s associated with one or more name spaces.
 * 
 * @author aerlach
 */
public interface ITemplateHandlerLibrary {

	/**
	 * Create a instance of a {@link ITemplateHandler} using the given {@code
	 * namespace}, and {@code}.
	 * 
	 * @param namespace
	 * @param localName
	 * @return a {@link ITemplateHandler} instance using the given {@code
	 *         namespace}, and {@code}
	 */
	ITemplateHandler createTemplateHandler(String namespace, String localName);

}
