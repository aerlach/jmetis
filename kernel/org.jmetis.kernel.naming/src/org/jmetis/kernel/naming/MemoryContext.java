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
import java.util.Map;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.OperationNotSupportedException;

/**
 * <tt>MemoryContext</tt> is a simple JNDI service provider. Objects bound into
 * it are placed into a Hashtable and are not persisted outside of the virtual
 * machine. The namespace is flat, and it is not federated in the sense of JNDI
 * federation.
 * <p/>
 * 
 * @author era
 */
public class MemoryContext extends AbstractContext {
	private String name;
	private Map bindings;

	public MemoryContext(Hashtable<String, String> environment) {
		this("/", environment); //$NON-NLS-1$
	}

	public MemoryContext(String name, Hashtable environment) {
		super(environment);
		this.name(name);
		this.bindings(this.newBindings());
	}

	protected String name() {
		return this.name;
	}

	protected void name(String name) {
		this.name = name;
	}

	/**
	 * @see javax.naming.Context#getNameInNamespace()
	 */
	public String getNameInNamespace() throws NamingException {
		return this.name();
	}

	protected Map bindings() {
		return this.bindings;
	}

	protected void bindings(Map bindings) {
		this.bindings = bindings;
	}

	protected Map newBindings() {
		return new Hashtable(11);
	}

	protected Object bindingAt(String name) throws NamingException {
		return this.bindings().get(name);
	}

	/**
	 * @see javax.naming.Context#getNameParser(java.lang.String)
	 */
	public NameParser getNameParser(String name) throws NamingException {
		return Constants.flatNameParser();
	}

	/**
	 * @see javax.naming.Context#getNameParser(javax.naming.Name)
	 */
	public NameParser getNameParser(Name name) throws NamingException {
		return this.getNameParser(name.toString());
	}

	/**
	 * @see javax.naming.Context#lookup(java.lang.String)
	 */
	public Object lookup(String name) throws NamingException {
		if ("".equals(name) || name.equals(this.name())) { //$NON-NLS-1$
			return this;
		}
		Object binding = this.bindingAt(name);
		if (binding == null) {
			throw new NameNotFoundException(name + " not found"); //$NON-NLS-1$
		}
		return binding;
	}

	/**
	 * @see javax.naming.Context#lookup(javax.naming.Name)
	 */
	public Object lookup(Name name) throws NamingException {
		return this.lookup(name.toString());
	}

	/**
	 * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
	 */
	public void bind(String name, Object object) throws NamingException {
		if (name.equals(this.name())) {
			throw new InvalidNameException("Cannot bind empty name"); //$NON-NLS-1$
		}
		if (this.bindings().containsKey(name)) {
			throw new NameAlreadyBoundException(
					"Use rebind to override <" + name + "> with <" + object.getClass().getName() + "@" + System.identityHashCode(object) + ">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		this.bindings().put(name, object);
	}

	/**
	 * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
	 */
	public void bind(Name name, Object object) throws NamingException {
		this.bind(name.toString(), object);
	}

	/**
	 * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
	 */
	public void rebind(String name, Object object) throws NamingException {
		if (name.equals(this.name())) {
			throw new InvalidNameException("Cannot bind empty name"); //$NON-NLS-1$
		}
		this.bindings().put(name, object);
	}

	/**
	 * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
	 */
	public void rebind(Name name, Object object) throws NamingException {
		this.rebind(name.toString(), object);
	}

	/**
	 * @see javax.naming.Context#unbind(java.lang.String)
	 */
	public void unbind(String name) throws NamingException {
		if (name.equals(this.name())) {
			throw new InvalidNameException("Cannot unbind empty name"); //$NON-NLS-1$
		}
		this.bindings().remove(name);
	}

	/**
	 * @see javax.naming.Context#unbind(javax.naming.Name)
	 */
	public void unbind(Name name) throws NamingException {
		this.unbind(name.toString());
	}

	/**
	 * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
	 */
	public void rename(String oldName, String newName) throws NamingException {
		if (oldName.equals(this.name()) || newName.equals(this.name())) {
			throw new InvalidNameException("Cannot rename empty name"); //$NON-NLS-1$
		}
		if (this.bindings().get(newName) != null) {
			throw new NameAlreadyBoundException(newName + " is already bound"); //$NON-NLS-1$
		}
		Object oldBinding = this.bindings().remove(oldName);
		if (oldBinding == null) {
			throw new NameNotFoundException(oldName + " not bound"); //$NON-NLS-1$
		}
		this.bindings().put(newName, oldBinding);
	}

	/**
	 * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
	 */
	public void rename(Name oldName, Name newName) throws NamingException {
		this.rename(oldName.toString(), newName.toString());
	}

	/**
	 * @see javax.naming.Context#list(java.lang.String)
	 */
	public NamingEnumeration list(String name) throws NamingException {
		if ("".equals(name)) { //$NON-NLS-1$
			return new Enumeration(this.bindings().entrySet());
		}
		Object target = this.lookup(name);
		if (target instanceof Context) {
			return ((Context) target).list(""); //$NON-NLS-1$
		}
		throw new NotContextException(name + " cannot be listed"); //$NON-NLS-1$
	}

	/**
	 * @see javax.naming.Context#list(javax.naming.Name)
	 */
	public NamingEnumeration list(Name name) throws NamingException {
		return this.list(name.toString());
	}

	/**
	 * @see javax.naming.Context#listBindings(java.lang.String)
	 */
	public NamingEnumeration listBindings(String name) throws NamingException {
		if ("".equals(name)) { //$NON-NLS-1$
			return new Enumeration(this.bindings().entrySet());
		}
		Object target = this.lookup(name);
		if (target instanceof Context) {
			return ((Context) target).listBindings(""); //$NON-NLS-1$
		}
		throw new NotContextException(name + " cannot be listed"); //$NON-NLS-1$
	}

	/**
	 * @see javax.naming.Context#listBindings(javax.naming.Name)
	 */
	public NamingEnumeration listBindings(Name name) throws NamingException {
		return this.listBindings(name.toString());
	}

	protected void errorSubcontextsNotSupported() throws NamingException {
		throw new OperationNotSupportedException("Does not support subcontexts"); //$NON-NLS-1$
	}

	/**
	 * @see javax.naming.Context#destroySubcontext(java.lang.String)
	 */
	public void destroySubcontext(String name) throws NamingException {
		this.errorSubcontextsNotSupported();
	}

	/**
	 * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
	 */
	public void destroySubcontext(Name name) throws NamingException {
		this.destroySubcontext(name.toString());
	}

	/**
	 * @see javax.naming.Context#createSubcontext(java.lang.String)
	 */
	public Context createSubcontext(String name) throws NamingException {
		this.errorSubcontextsNotSupported();
		return null;
	}

	/**
	 * @see javax.naming.Context#createSubcontext(javax.naming.Name)
	 */
	public Context createSubcontext(Name name) throws NamingException {
		return this.createSubcontext(name.toString());
	}

	/**
	 * @see javax.naming.Context#lookupLink(java.lang.String)
	 */
	public Object lookupLink(String name) throws NamingException {
		return this.lookup(name);
	}

	/**
	 * @see javax.naming.Context#lookupLink(javax.naming.Name)
	 */
	public Object lookupLink(Name name) throws NamingException {
		return this.lookupLink(name.toString());
	}

	/**
	 * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
	 */
	public String composeName(String name, String prefix)
			throws NamingException {
		Name result = this.composeName(new CompositeName(name),
				new CompositeName(prefix));
		return result.toString();
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
}
