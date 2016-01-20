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
package org.jmetis.binding;

import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code BindingException}
 * 
 * TODO subclass ELException
 * 
 * @author aerlach
 */
public class BindingException extends RuntimeException {

	private static final long serialVersionUID = 3532484789924300865L;

	/**
	 * Constructs a new {@code BindingException} instance.
	 * 
	 * @param code
	 * @param arguments
	 */
	public BindingException(String code, Object... arguments) {
		super(code);
	}

	/**
	 * Constructs a new {@code BindingException} instance.
	 * 
	 * @param code
	 * @param cause
	 * @param arguments
	 */
	public BindingException(String code, Throwable cause, Object... arguments) {
		super(code, cause);
	}

	public static void propertyNotFound(Class<?> declaringClass,
			String propertyName) {
		throw new BindingException("propertyNotFound"); //$NON-NLS-1$
	}

	public static void cannotAddPropertyChangeListener(Object model,
			IPropertyDescription propertyDescriptor, Throwable exception) {
		throw new BindingException("cannotAddPropertyChangeListener", exception); //$NON-NLS-1$
	}

	public static void cannotRemovePropertyChangeListener(Object model,
			IPropertyDescription propertyDescriptor, Throwable exception) {
		throw new BindingException(
				"cannotRemovePropertyChangeListener", exception); //$NON-NLS-1$
	}

	public static void cannotReadProperty(Object model,
			IPropertyDescription propertyDescriptor) {
		throw new BindingException("cannotReadProperty"); //$NON-NLS-1$
	}

	public static void readPropertyFailed(Object model,
			IPropertyDescription propertyDescriptor, Throwable exception) {
		throw new BindingException("readPropertyFailed", exception);
	}

	public static void cannotWriteProperty(Object model, Object value,
			IPropertyDescription propertyDescriptor) {
		throw new BindingException("cannotWriteProperty");
	}

	public static void writePropertyFailed(Object model, Object value,
			IPropertyDescription propertyDescriptor, Throwable exception) {
		throw new BindingException("writePropertyFailed", exception);
	}

}
