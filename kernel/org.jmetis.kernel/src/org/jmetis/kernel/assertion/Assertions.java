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
package org.jmetis.kernel.assertion;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * {@code Assertions} contains utility functions for assertions.
 * 
 * @author era
 */
public class Assertions {

	private static ResourceBundle LOCALIZED_MESSAGES = ResourceBundle
	.getBundle("/localization");

	/**
	 * {@code Assertions} should not be instantiated.
	 */
	protected Assertions() {
		super();
	}

	protected static String localizedString(String key, Object... arguments) {
		try {
			String message = Assertions.LOCALIZED_MESSAGES.getString(key);
			return String.format(message, arguments);
		} catch (MissingResourceException ex) {
			StringBuilder defaultMessage = new StringBuilder(key.length()
					+ arguments.length * 32);
			defaultMessage.append(key);
			if (arguments.length > 0) {
				defaultMessage.append('(').append(arguments[0]);
				for (int i = 1, n = arguments.length; i < n; i++) {
					defaultMessage.append(arguments[i]);
				}
				defaultMessage.append(')');
			}
			return defaultMessage.toString();
		}
	}

	/**
	 * Asserts that the given {@code value} is not {@code null}. If it is
	 * {@code null} an {@link NullPointerException} is thrown with the given
	 * {@code name}.
	 */
	public static <T> T mustNotBeNull(String name, T value) {
		if (value == null) {
			throw new NullPointerException(Assertions.localizedString(
					"mustNotBeNull", name)); //$NON-NLS-1$
		}
		return value;
	}

	public static String mustNotBeNullOrEmpty(String name, String value) {
		if (Assertions.mustNotBeNull(name, value).length() == 0) {
			throw new IllegalArgumentException(Assertions.localizedString(
					"mustNotBeEmpty", name)); //$NON-NLS-1$
		}
		return value;
	}

	public static <E> E[] mustNotBeNullOrEmpty(String name, E[] value) {
		if (Assertions.mustNotBeNull(name, value).length == 0) {
			throw new IllegalArgumentException(Assertions.localizedString(
					"mustNotBeEmpty", name)); //$NON-NLS-1$
		}
		return value;
	}

	public static void mustNotBeReached(Exception exception) {
		throw new IllegalStateException(Assertions
				.localizedString("unreachableExecutionPath"), exception); //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> mustBeAssignableTo(String name,
			Class<?> actualType, Class<T> expectedType) {
		if (!expectedType.isAssignableFrom(actualType)) {
			throw new ClassCastException(Assertions.localizedString(
					"mustBeAssignableFrom", expectedType, //$NON-NLS-1$
					actualType));
		}
		return (Class<T>) actualType;
	}

	@SuppressWarnings("unchecked")
	public static <T> T mustBeAssignableTo(String name, Object value,
			Class<T> expectedType) {
		if (value == null) {
			return null;
		}
		Class<?> actualType = value.getClass();
		if (!expectedType.isAssignableFrom(actualType)) {
			throw new ClassCastException(Assertions.localizedString(
					"mustBeAssignableFrom", expectedType, //$NON-NLS-1$
					actualType));
		}
		return (T) value;
	}

}
