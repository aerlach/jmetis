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
package org.jmetis.test;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * {@code Primitives}
 * 
 * @author aerlach
 */
public class Primitives {

	/**
	 * Constructs a new {@code Primitives} instance.
	 * 
	 */
	protected Primitives() {
		super();
	}

	/**
	 * Ensures that the bundle with the specified name and the highest available
	 * version is started.
	 */
	public static Bundle ensureBundleStarted(String symbolicName)
			throws Exception {
		Bundle bundle = Platform.getBundle(symbolicName);
		Dictionary<String, Object> bundleHeaders = bundle.getHeaders();
		for (Enumeration<String> it = bundleHeaders.keys(); it
				.hasMoreElements();) {
			String key = it.nextElement();
			Object value = bundleHeaders.get(key);
			if (value != null && value.getClass().isArray()) {
				System.out
						.println(key + ": " + Arrays.asList((Object[]) value));
			} else {
				System.out.println(key + ": " + value);
			}
		}
		if (bundle == null) {
			throw new IllegalStateException("Bundle " + symbolicName
					+ " not found");
		}
		if (bundle != null && bundle.getState() != Bundle.ACTIVE) {
			bundle.start(Bundle.START_TRANSIENT);
		}
		return bundle;
	}

}
