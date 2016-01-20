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
package org.jmetis.messaging.osgi;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * {@code Primitives}
 * 
 * @author era
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
	 * Returns the resolved bundle with the specified symbolic name that has the
	 * highest version. If no resolved bundles are installed that have the
	 * specified symbolic name then null is returned.
	 * 
	 * @param symbolicName
	 *            the symbolic name of the bundle to be returned.
	 * @return the bundle that has the specified symbolic name with the highest
	 *         version, or {@code null} if no bundle is found.
	 */
	public static Bundle bundleNamed(String symbolicName) {
		return Platform.getBundle(symbolicName);
	}

	/**
	 * Ensures that the bundle with the specified name and the highest available
	 * version is started.
	 */
	public static void ensureBundleStarted(String symbolicName)
			throws Exception {
		Bundle bundle = Primitives.bundleNamed(symbolicName);
		if (bundle != null && bundle.getState() != Bundle.ACTIVE) {
			bundle.start(Bundle.START_TRANSIENT);
		}
	}

}
