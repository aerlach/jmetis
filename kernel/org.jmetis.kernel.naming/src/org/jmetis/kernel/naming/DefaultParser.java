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
package org.jmetis.kernel.naming;

import java.util.Properties;

import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

/**
 * The <tt>DefaultParser</tt> implements the {@link javax.naming.NameParser
 * NameParser} interface.
 * <p/>
 * 
 * @author era
 */
public class DefaultParser implements NameParser {
	private Properties syntax;

	/**
	 * Constructs a new <tt>DefaultParser</tt> with the given <tt>syntax</tt>.
	 * 
	 * @param syntax
	 *            the syntax properties of the receiver.
	 */
	public DefaultParser(Properties syntax) {
		super();
		this.syntax(syntax);
	}

	/**
	 * @return the syntax properties of the receiver.
	 */
	protected Properties syntax() {
		return this.syntax;
	}

	/**
	 * @param syntax
	 *            the syntax properties to set.
	 */
	protected void syntax(Properties syntax) {
		this.syntax = syntax;
	}

	/**
	 * @see javax.naming.NameParser#parse(java.lang.String)
	 */
	public Name parse(String name) throws NamingException {
		return new CompoundName(name, this.syntax());
	}
}
