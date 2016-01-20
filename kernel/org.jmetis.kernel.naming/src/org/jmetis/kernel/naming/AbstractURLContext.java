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

import javax.naming.CannotProceedException;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.spi.NamingManager;
import javax.naming.spi.ResolveResult;

/**
 * The <tt>AbstractURLContext</tt> implements the default behavior for creating
 * a JNDI URLContext.
 * <p/>
 * 
 * @author era
 */
public abstract class AbstractURLContext extends AbstractContext {
	private String urlPrefix;
	private Context rootContext;

	/**
	 * Constructs a new <tt>AbstractURLContext</tt> with the given naming
	 * <tt>environment</tt>, and the given <tt>urlPrefix</tt>.
	 * 
	 * @param environment
	 *            the naming environment of the receiver.
	 * @param urlPrefix
	 *            the url prefix the receiver is responsible for.
	 */
	protected AbstractURLContext(Hashtable environment, String urlPrefix) {
		super(environment);
		this.urlPrefix(urlPrefix);
	}

	/**
	 * @return the url prefix of the receiver.
	 */
	protected String urlPrefix() {
		return this.urlPrefix;
	}

	/**
	 * @param urlPrefix
	 *            the url prefix to set.
	 */
	protected void urlPrefix(String urlPrefix) {
		this.assertIsNotNull("urlPrefix", urlPrefix); //$NON-NLS-1$
		this.urlPrefix = urlPrefix;
	}

	/**
	 * @return the root context of the receiver.
	 */
	protected Context rootContext() {
		return this.rootContext;
	}

	/**
	 * @param rootContext
	 *            the root context to set.
	 */
	protected void rootContext(Context rootContext) {
		this.rootContext = rootContext;
	}

	protected Context newRootContext(String url) throws NamingException {
		throw new NamingException(
				"Url context for <" + url + "> cannot be resolved"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected Context rootContext(String url) throws NamingException {
		Context rootContext = this.rootContext();
		if (rootContext == null) {
			rootContext = this.newRootContext(url);
		}
		return rootContext;
	}

	protected ResolveResult resolvedUrlContext(String url)
			throws NamingException {
		if (!url.startsWith(this.urlPrefix())) {
			throw new IllegalArgumentException("invalid url <" + url + ">"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		CompositeName remainingName = new CompositeName();
		int prefixLength = this.urlPrefix().length();
		if (url.length() > prefixLength) {
			remainingName.add(url.substring(prefixLength));
		}
		return new ResolveResult(this.rootContext(url), remainingName);
	}

	/**
	 * Returns the context in which to continue for the given multicomponent
	 * <tt>name</tt>.
	 */
	protected Context continuationContext(Name name) throws NamingException {
		Object obj = this.lookup(name.get(0));
		CannotProceedException exception = new CannotProceedException();
		exception.setResolvedObj(obj);
		exception.setEnvironment(this.environment());
		return NamingManager.getContinuationContext(exception);
	}

	/**
	 * @see javax.naming.Context#lookup(java.lang.String)
	 */
	public Object lookup(String name) throws NamingException {
		ResolveResult resolveResult = this.resolvedUrlContext(name);
		Context context = (Context) resolveResult.getResolvedObj();
		try {
			return context.lookup(resolveResult.getRemainingName());
		} finally {
			context.close();
		}
	}

	/**
	 * @see javax.naming.Context#lookup(javax.naming.Name)
	 */
	public Object lookup(Name name) throws NamingException {
		if (name.size() == 1) {
			return this.lookup(name.get(0));
		} else {
			Context context = this.continuationContext(name);
			try {
				return context.lookup(name.getSuffix(1));
			} finally {
				context.close();
			}
		}
	}

	/**
	 * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
	 */
	public void bind(String name, Object obj) throws NamingException {
		ResolveResult resolveResult = this.resolvedUrlContext(name);
		Context context = (Context) resolveResult.getResolvedObj();
		try {
			context.bind(resolveResult.getRemainingName(), obj);
		} finally {
			context.close();
		}
	}

	/**
	 * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
	 */
	public void bind(Name name, Object obj) throws NamingException {
		if (name.size() == 1) {
			this.bind(name.get(0), obj);
		} else {
			Context context = this.continuationContext(name);
			try {
				context.bind(name.getSuffix(1), obj);
			} finally {
				context.close();
			}
		}
	}

	/**
	 * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
	 */
	public void rebind(String name, Object obj) throws NamingException {
		ResolveResult resolveResult = this.resolvedUrlContext(name);
		Context context = (Context) resolveResult.getResolvedObj();
		try {
			context.rebind(resolveResult.getRemainingName(), obj);
		} finally {
			context.close();
		}
	}

	/**
	 * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
	 */
	public void rebind(Name name, Object obj) throws NamingException {
		if (name.size() == 1) {
			this.rebind(name.get(0), obj);
		} else {
			Context context = this.continuationContext(name);
			try {
				context.rebind(name.getSuffix(1), obj);
			} finally {
				context.close();
			}
		}
	}

	/**
	 * @see javax.naming.Context#unbind(java.lang.String)
	 */
	public void unbind(String name) throws NamingException {
		ResolveResult resolveResult = this.resolvedUrlContext(name);
		Context context = (Context) resolveResult.getResolvedObj();
		try {
			context.unbind(resolveResult.getRemainingName());
		} finally {
			context.close();
		}
	}

	/**
	 * @see javax.naming.Context#unbind(javax.naming.Name)
	 */
	public void unbind(Name name) throws NamingException {
		if (name.size() == 1) {
			this.unbind(name.get(0));
		} else {
			Context context = this.continuationContext(name);
			try {
				context.unbind(name.getSuffix(1));
			} finally {
				context.close();
			}
		}
	}

	protected String urlPrefixOf(String url) throws NamingException {
		int start = url.indexOf(":"); //$NON-NLS-1$
		if (start < 0) {
			throw new OperationNotSupportedException(
					"illegal url <" + url + ">"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		++start;
		if (url.startsWith("//", start)) { //$NON-NLS-1$
			start += 2;
			int end = url.indexOf("/", start); //$NON-NLS-1$
			if (end >= 0) {
				start = end;
			} else {
				start = url.length();
			}
		}
		return url.substring(0, start);
	}

	/**
	 * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
	 */
	public void rename(String oldName, String newName) throws NamingException {
		String newPrefix = this.urlPrefixOf(newName);
		if (!oldName.startsWith(newPrefix)) {
			throw new OperationNotSupportedException(
					"prefix must match <" + oldName + ">, <" + newName + ">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		ResolveResult resolveResult = this.resolvedUrlContext(oldName);
		Context context = (Context) resolveResult.getResolvedObj();
		try {
			int splitIndex = newPrefix.length();
			splitIndex += newName.charAt(splitIndex) == '/' ? 1 : 0;
			Name newRemainingName = new CompositeName();
			if (splitIndex != newName.length()) {
				newRemainingName.add(newName.substring(splitIndex));
			}
			context.rename(resolveResult.getRemainingName(), newRemainingName);
		} finally {
			context.close();
		}
	}

	/**
	 * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
	 */
	public void rename(Name oldName, Name newName) throws NamingException {
		String oldPrefix = oldName.get(0);
		String newPrefix = newName.get(0);
		if (oldName.size() == 1 && newName.size() == 1) {
			this.rename(oldPrefix, newPrefix);
		} else {
			if (!oldPrefix.equals(newPrefix)) {
				throw new OperationNotSupportedException(
						"prefix must match <" + oldName + ">, <" + newName + ">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			Context context = this.continuationContext(oldName);
			try {
				context.rename(oldName.getSuffix(1), newName.getSuffix(1));
			} finally {
				context.close();
			}
		}
	}

	/**
	 * @see javax.naming.Context#list(java.lang.String)
	 */
	public NamingEnumeration list(String name) throws NamingException {
		ResolveResult resolveResult = this.resolvedUrlContext(name);
		Context context = (Context) resolveResult.getResolvedObj();
		try {
			return context.list(resolveResult.getRemainingName());
		} finally {
			context.close();
		}
	}

	/**
	 * @see javax.naming.Context#list(javax.naming.Name)
	 */
	public NamingEnumeration list(Name name) throws NamingException {
		if (name.size() == 1) {
			return this.list(name.get(0));
		} else {
			Context context = this.continuationContext(name);
			try {
				return context.list(name.getSuffix(1));
			} finally {
				context.close();
			}
		}
	}

	/**
	 * @see javax.naming.Context#listBindings(java.lang.String)
	 */
	public NamingEnumeration listBindings(String name) throws NamingException {
		ResolveResult resolveResult = this.resolvedUrlContext(name);
		Context context = (Context) resolveResult.getResolvedObj();
		try {
			return context.listBindings(resolveResult.getRemainingName());
		} finally {
			context.close();
		}
	}

	/**
	 * @see javax.naming.Context#listBindings(javax.naming.Name)
	 */
	public NamingEnumeration listBindings(Name name) throws NamingException {
		if (name.size() == 1) {
			return this.listBindings(name.get(0));
		} else {
			Context context = this.continuationContext(name);
			try {
				return context.listBindings(name.getSuffix(1));
			} finally {
				context.close();
			}
		}
	}

	/**
	 * @see javax.naming.Context#destroySubcontext(java.lang.String)
	 */
	public void destroySubcontext(String name) throws NamingException {
		ResolveResult resolveResult = this.resolvedUrlContext(name);
		Context context = (Context) resolveResult.getResolvedObj();
		try {
			context.destroySubcontext(resolveResult.getRemainingName());
		} finally {
			context.close();
		}
	}

	/**
	 * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
	 */
	public void destroySubcontext(Name name) throws NamingException {
		if (name.size() == 1) {
			this.destroySubcontext(name.get(0));
		} else {
			Context context = this.continuationContext(name);
			try {
				context.destroySubcontext(name.getSuffix(1));
			} finally {
				context.close();
			}
		}
	}

	/**
	 * @see javax.naming.Context#createSubcontext(java.lang.String)
	 */
	public Context createSubcontext(String name) throws NamingException {
		ResolveResult resolveResult = this.resolvedUrlContext(name);
		Context context = (Context) resolveResult.getResolvedObj();
		try {
			return context.createSubcontext(resolveResult.getRemainingName());
		} finally {
			context.close();
		}
	}

	/**
	 * @see javax.naming.Context#createSubcontext(javax.naming.Name)
	 */
	public Context createSubcontext(Name name) throws NamingException {
		if (name.size() == 1) {
			return this.createSubcontext(name.get(0));
		} else {
			Context context = this.continuationContext(name);
			try {
				return context.createSubcontext(name.getSuffix(1));
			} finally {
				context.close();
			}
		}
	}

	/**
	 * @see javax.naming.Context#lookupLink(java.lang.String)
	 */
	public Object lookupLink(String name) throws NamingException {
		ResolveResult resolveResult = this.resolvedUrlContext(name);
		Context context = (Context) resolveResult.getResolvedObj();
		try {
			return context.lookupLink(resolveResult.getRemainingName());
		} finally {
			context.close();
		}
	}

	/**
	 * @see javax.naming.Context#lookupLink(javax.naming.Name)
	 */
	public Object lookupLink(Name name) throws NamingException {
		if (name.size() == 1) {
			return this.lookupLink(name.get(0));
		} else {
			Context context = this.continuationContext(name);
			try {
				return context.lookupLink(name.getSuffix(1));
			} finally {
				context.close();
			}
		}
	}

	/**
	 * @see javax.naming.Context#getNameParser(java.lang.String)
	 */
	public NameParser getNameParser(String name) throws NamingException {
		ResolveResult resolveResult = this.resolvedUrlContext(name);
		Context context = (Context) resolveResult.getResolvedObj();
		try {
			return context.getNameParser(resolveResult.getRemainingName());
		} finally {
			context.close();
		}
	}

	/**
	 * @see javax.naming.Context#getNameParser(javax.naming.Name)
	 */
	public NameParser getNameParser(Name name) throws NamingException {
		if (name.size() == 1) {
			return this.getNameParser(name.get(0));
		} else {
			Context context = this.continuationContext(name);
			try {
				return context.getNameParser(name.getSuffix(1));
			} finally {
				context.close();
			}
		}
	}

	/**
	 * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
	 */
	public String composeName(String name, String prefix)
			throws NamingException {
		if (prefix.equals("")) { //$NON-NLS-1$
			return name;
		} else if (name.equals("")) { //$NON-NLS-1$
			return prefix;
		} else {
			return prefix + "/" + name; //$NON-NLS-1$
		}
	}

	/**
	 * @see javax.naming.Context#composeName(javax.naming.Name,
	 *      javax.naming.Name)
	 */
	public Name composeName(Name name, Name prefix) throws NamingException {
		Name result = (Name) prefix.clone();
		result.addAll(name);
		return result;
	}

	/**
	 * @return "" according to the specification.
	 * @see javax.naming.Context#getNameInNamespace()
	 */
	public String getNameInNamespace() throws NamingException {
		return ""; //$NON-NLS-1$
	}
}
