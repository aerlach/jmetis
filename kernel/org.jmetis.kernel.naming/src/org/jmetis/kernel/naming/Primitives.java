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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkRef;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * Package primitives.
 * <p/>
 * 
 * @author era
 */
public class Primitives {
	private static Map<String, String> environmentVariables;

	/**
	 * Protected constructor - <tt>Primitives</tt> must not be instantiated.
	 */
	protected Primitives() {
		super();
	}

	protected static String preferredString(String context, String key,
			String defaultValue) {
		return defaultValue;
	}

	/**
	 * Returns the value associated with the given <tt>key</tt> in the
	 * configuration for the receiver. Returns the given <tt>defaultValue</tt>
	 * if there is no value associated with the given <tt>key</tt>.
	 * 
	 * @param context
	 *            the context is used to determine the preferences node.
	 * @param key
	 *            key whose associated value is to be returned.
	 * @param defaultValue
	 *            the value to be returned in the event that this preference
	 *            node has no value associated with <tt>key</tt>.
	 * @return the value associated with <tt>key</tt>, or <tt>defaultValue</tt>
	 *         if no value is associated with <tt>key</tt>.
	 * @throws java.lang.IllegalArgumentException
	 *             if <tt>key</tt> is <tt>null</tt>. A <tt>null</tt> value for
	 *             <tt>defaultValue</tt> <i>is</i> permitted.
	 */
	public static String preferredString(Object context, String key,
			String defaultValue) {
		Class<?> currentClass;
		if (context instanceof String) {
			return Primitives.preferredString(context, key, defaultValue);
		} else if (context instanceof Class) {
			currentClass = (Class<?>) context;
		} else if (context != null) {
			currentClass = context.getClass();
		} else {
			return Primitives.preferredString((String) null, key, defaultValue);
		}
		while (currentClass != Object.class) {
			String value = Primitives.preferredString(currentClass.getName(),
					key, (String) null);
			if (value != null) {
				return value;
			}
			currentClass = currentClass.getSuperclass();
		}
		currentClass = context instanceof Class ? (Class<?>) context : context
				.getClass();
		String className = currentClass.getName();
		int index = className.lastIndexOf('.');
		return Primitives.preferredString(index > 0 ? className.substring(0,
				index) : (String) null, key, defaultValue);
	}

	protected static String printEnvironmentCommand() {
		String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
		if (osName.indexOf("windows 9") > -1) { // Windows 9x //$NON-NLS-1$
			return "command.com /c set"; //$NON-NLS-1$
		} else if (osName.indexOf("nt") > -1 || osName.indexOf("windows") > -1) { // Windows NT or newer //$NON-NLS-1$ //$NON-NLS-2$
			return "cmd.exe /c set"; //$NON-NLS-1$
		} else { // Unix
			return "env"; //$NON-NLS-1$
		}
	}

	protected static Map<String, String> newEnvironmentVariables() {
		Map<String, String> environmentVariables = new HashMap<String, String>();
		try {
			Process process = Runtime.getRuntime().exec(
					Primitives.printEnvironmentCommand());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					int index = line.indexOf('=');
					String key = line.substring(0, index);
					String value = line.substring(index + 1);
					environmentVariables.put(key, value);
				}
			} finally {
				reader.close();
			}
		} catch (IOException ex) {
		}
		return Collections.unmodifiableMap(environmentVariables);
	}

	public static Map<String, String> environmentVariables() {
		if (Primitives.environmentVariables == null) {
			Primitives.environmentVariables = Primitives
					.newEnvironmentVariables();
		}
		return Primitives.environmentVariables;
	}

	/**
	 * @param key
	 *            the name of the environment variable.
	 * @param defaultValue
	 *            the default value.
	 * @return the value of the environment variable with the given
	 *         <tt>name</tt>. If the environment variable does not exist the
	 *         given <tt>defaultValue</tt> is returned.
	 */
	public static String environmentVariable(String key, String defaultValue) {
		String value = Primitives.environmentVariables().get(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * @param preferencesContext
	 *            the object used to gather the naming properties.
	 * @return the naming environment for the given <tt>preferencesContext</tt>.
	 */
	public static Hashtable<String, String> namingEnvironment(
			Object preferencesContext) {
		Hashtable<String, String> environment = new Hashtable<String, String>();
		if (preferencesContext != null) {
			environment
					.put(
							Context.INITIAL_CONTEXT_FACTORY,
							Primitives
									.preferredString(
											preferencesContext,
											"contextFactory", "weblogic.jndi.WLInitialContextFactory")); //$NON-NLS-1$ //$NON-NLS-2$
			environment.put(Context.PROVIDER_URL, Primitives.preferredString(
					preferencesContext, "provider_url", "t3://localhost:7100")); //$NON-NLS-1$ //$NON-NLS-2$
			environment.put(Context.SECURITY_PRINCIPAL, Primitives
					.preferredString(preferencesContext,
							"security_principal", "system")); //$NON-NLS-1$ //$NON-NLS-2$ 
			environment.put(Context.SECURITY_CREDENTIALS, Primitives
					.preferredString(preferencesContext,
							"security_credentials", "metsys")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}
		return environment;
	}

	public static Context acquireContext(Object preferencesContext)
			throws NamingException {
		return new InitialContext(Primitives
				.namingEnvironment(preferencesContext));
	}

	public static Context acquireContext() throws NamingException {
		return Primitives.acquireContext(null);
	}

	public static void releaseContext(Context namingContext) {
		try {
			namingContext.close();
		} catch (NamingException ex) {
		}
	}

	public static Object objectNamed(Object preferencesContext, String name)
			throws NamingException {
		Context namingContext = Primitives.acquireContext(preferencesContext);
		try {
			return namingContext.lookup(name);
		} finally {
			Primitives.releaseContext(namingContext);
		}
	}

	public static Object objectNamed(String name) throws NamingException {
		return Primitives.objectNamed(null, name);
	}

	protected static void dumpContentsOf(Map<?, ?> map) {
		SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>(map);
		Set<Map.Entry<Object, Object>> entrySet = sortedMap.entrySet();
		for (Map.Entry<Object, Object> entry : entrySet) {
			System.out.println(entry.getKey() + " = " + entry.getValue()); //$NON-NLS-1$
		}
	}

	public static void dumpEnvironmentOf(Context context)
			throws NamingException {
		Primitives.dumpContentsOf(context.getEnvironment());
	}

	public static void dumpContentsOf(NamingEnumeration<NameClassPair> bindings)
			throws NamingException {
		while (bindings.hasMore()) {
			NameClassPair entry = bindings.next();
			System.out.println(entry.getName() + " = " + entry.getClassName()); //$NON-NLS-1$
		}
	}

	public static void dumpContentsOf(Context context) throws NamingException {
		StringBuffer buffer = new StringBuffer(1024);
		Primitives.dumpContentsOn(context, " ", buffer); //$NON-NLS-1$
		System.out.println(buffer.toString());
	}

	protected static void dumpContentsOn(Context context, String indent,
			StringBuffer buffer) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			NamingEnumeration<NameClassPair> it = context.list(""); //$NON-NLS-1$
			while (it.hasMore()) {
				NameClassPair pair = it.next();
				String name = pair.getName();
				String className = pair.getClassName();
				try {
					Class<?> objectClass = loader.loadClass(className);
					buffer.append(indent + " +- " + name); //$NON-NLS-1$
					if (LinkRef.class.isAssignableFrom(objectClass)) {
						try {
							Object obj = context.lookupLink(name);

							LinkRef link = (LinkRef) obj;
							buffer.append("[link -> "); //$NON-NLS-1$
							buffer.append(link.getLinkName());
							buffer.append(']');
						} catch (Throwable ex) {
							buffer.append("[invalid]"); //$NON-NLS-1$
						}
					} else {
						buffer.append(" (class: " + pair.getClassName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					buffer.append('\n');
					if (Context.class.isAssignableFrom(objectClass)) {
						try {
							Object value = context.lookup(name);
							if (value instanceof Context) {
								Context subcontext = (Context) value;
								Primitives.dumpContentsOn(subcontext, indent
										+ " |  ", buffer); //$NON-NLS-1$
							} else {
								buffer.append(indent
										+ " |   NonContext: " + value); //$NON-NLS-1$
								buffer.append('\n');
							}
						} catch (Throwable ex) {
							buffer
									.append("Failed to lookup: " + name + ", errmsg = " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
							buffer.append('\n');
						}
					}
				} catch (ClassNotFoundException ex) {
				}
			}
			it.close();
		} catch (NamingException ex) {
			buffer
					.append("error while listing context " + context.toString() + ": " + ex.toString(true)); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public static void dumpNamingContext(Context context)
			throws NamingException {
		synchronized (System.out) {
			try {
				context.list("");} catch (Exception ex) {} //$NON-NLS-1$
			System.out
					.println("Dump of context <" + context.getNameInNamespace() + ">"); //$NON-NLS-1$ //$NON-NLS-2$
			System.out
					.println("- Environment ----------------------------------------------------------------"); //$NON-NLS-1$
			Primitives.dumpEnvironmentOf(context);
			System.out
					.println("- Bindings -------------------------------------------------------------------"); //$NON-NLS-1$
			Primitives.dumpContentsOf(context);
			System.out
					.println("------------------------------------------------------------------------------"); //$NON-NLS-1$
		}
	}

	public static void dumpNamingContext(Hashtable<String, String> environment)
			throws NamingException {
		Primitives.dumpNamingContext(new InitialContext(environment));
	}

	public static void dumpNamingContext() throws NamingException {
		Primitives.dumpNamingContext(new Hashtable<String, String>());
	}
}