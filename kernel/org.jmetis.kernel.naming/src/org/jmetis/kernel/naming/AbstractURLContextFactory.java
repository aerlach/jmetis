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
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

/**
 * The <tt>AbstractURLContextFactory</tt> implements the default behavior of a
 * URL context factory.
 * <p/>
 * 
 * @author era
 */
public abstract class AbstractURLContextFactory implements ObjectFactory {
	protected AbstractURLContextFactory() {
		super();
	}

	protected abstract Context rootContextFor(Hashtable environment)
			throws Exception;

	/**
	 * @see javax.naming.spi.ObjectFactory#getObjectInstance(java.lang.Object,
	 *      javax.naming.Name, javax.naming.Context, java.util.Hashtable)
	 */
	public Object getObjectInstance(Object url, Name name, Context context,
			Hashtable environment) throws Exception {
		if (url == null) {
			return this.rootContextFor(environment);
		}
		if (url instanceof String) {
			Context urlContext = this.rootContextFor(environment);
			try {
				return urlContext.lookup((String) url);
			} finally {
				urlContext.close();
			}
		}
		if (url instanceof String[]) {
			String[] urls = (String[]) url;
			Context urlContext = this.rootContextFor(environment);
			try {
				NamingException exception = null;
				int count = urls.length;
				for (int i = 0; i < count; i++) {
					try {
						return urlContext.lookup(urls[i]);
					} catch (NamingException ex) {
						exception = ex;
					}
				}
				throw exception;
			} finally {
				urlContext.close();
			}
		}
		return null;
	}
}
