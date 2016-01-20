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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;

import org.jmetis.kernel.assertion.Assertions;

/**
 * The <tt>AbstractContext</tt> implements the default behavior of the
 * {@link javax.naming.Context Context} interface.
 * <p/>
 * 
 * @author era
 */
public abstract class AbstractContext implements Context {
	private Hashtable environment;

	/**
	 * Constructs a new <tt>AbstractContext</tt> with the given naming
	 * <tt>environment</tt>.
	 * 
	 * @param environment
	 *            the naming environment of the receiver.
	 */
	protected AbstractContext(Hashtable environment) {
		super();
		this.environment(environment);
	}

	/**
	 * @see at.spardat.mbs.common.core.Assertions#isNotNull(java.lang.String,
	 *      java.lang.Object)
	 */
	protected void assertIsNotNull(String identifier, Object value)
			throws IllegalArgumentException {
		Assertions.mustNotBeNull(identifier, value);
	}

	/**
	 * @return the naming environment of the receiver.
	 */
	protected Hashtable environment() {
		return this.environment;
	}

	/**
	 * @param environment
	 *            the naming environment to set.
	 */
	protected void environment(Hashtable environment) {
		this.environment = environment != null ? (Hashtable) environment
				.clone() : new Hashtable(5, 0.75f);
	}

	/**
	 * @see javax.naming.Context#addToEnvironment(java.lang.String,
	 *      java.lang.Object)
	 */
	public Object addToEnvironment(String key, Object value)
			throws NamingException {
		return this.environment().put(key, value);
	}

	/**
	 * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
	 */
	public Object removeFromEnvironment(String key) throws NamingException {
		return this.environment().remove(key);
	}

	/**
	 * @see javax.naming.Context#getEnvironment()
	 */
	public Hashtable getEnvironment() throws NamingException {
		return (Hashtable) this.environment().clone();
	}

	/**
	 * @see javax.naming.Context#close()
	 */
	public void close() throws NamingException {
	}
}
