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

import java.util.HashMap;
import java.util.Map;

/**
 * {@code DiagnosticContext} is similar to the Log4J Mapped Diagnostic Context
 * class except that it is based on a {@link InheritableThreadLocal}.
 * 
 * @author aerlach
 */
public class DiagnosticContext {

	private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL;

	private DiagnosticContext() {
		super();
	}

	/**
	 * Get the diagnostic context of the current thread.
	 * */
	protected static Map<String, Object> getCurrentContext() {
		return DiagnosticContext.THREAD_LOCAL.get();
	}

	/**
	 * Put a context value (the {@code value} parameter) as identified with the
	 * {@code key} parameter into the current thread's context map.
	 */
	public static void put(String key, Object value) {
		DiagnosticContext.getCurrentContext().put(key, value);
	}

	/**
	 * Get the context identified by the {@code key} parameter.
	 */
	public static Object get(String key) {
		return DiagnosticContext.getCurrentContext().get(key);
	}

	/**
	 * Remove the the context identified by the {@code key} parameter.
	 */
	public static void remove(String key) {
		DiagnosticContext.getCurrentContext().remove(key);
	}

	static {
		THREAD_LOCAL = new InheritableThreadLocal<Map<String, Object>>() {
			@Override
			protected Map<String, Object> initialValue() {
				return new HashMap<String, Object>();
			}

		};
	}
}
