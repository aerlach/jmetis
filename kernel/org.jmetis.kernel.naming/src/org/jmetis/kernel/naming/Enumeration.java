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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * The <tt>Enumeration</tt> implements the
 * {@link javax.naming.NamingEnumeration NamingEnumeration} interface based on
 * {@link java.util.Map#entrySet() entrySet}.
 * <p/>
 * 
 * @author era
 */
public class Enumeration implements NamingEnumeration {
	protected Iterator bindings;

	public Enumeration(Set entrySet) {
		super();
		this.bindings = entrySet.iterator();
	}

	/**
	 * @see java.util.Enumeration#hasMoreElements()
	 */
	public boolean hasMoreElements() {
		return this.bindings.hasNext();
	}

	/**
	 * @see javax.naming.NamingEnumeration#hasMore()
	 */
	public boolean hasMore() throws NamingException {
		return this.hasMoreElements();
	}

	protected Object newElement(String name, Object object) {
		return new Binding(name, object);
	}

	/**
	 * @see java.util.Enumeration#nextElement()
	 */
	public Object nextElement() {
		Map.Entry entry = (Map.Entry) this.bindings.next();
		return this.newElement((String) entry.getKey(), entry.getValue());
	}

	/**
	 * @see javax.naming.NamingEnumeration#next()
	 */
	public Object next() throws NamingException {
		return this.nextElement();
	}

	/**
	 * @see javax.naming.NamingEnumeration#close()
	 */
	public void close() {
	}
}
