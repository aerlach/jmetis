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

import java.util.Properties;

import javax.naming.NameParser;

/**
 * Package constants.
 * <p/>
 * 
 * @author era
 */
public class Constants {
	private static NameParser flatParser;

	/**
	 * Protected constructor - <tt>Constants</tt> must not be instantiated.
	 */
	protected Constants() {
		super();
	}

	/**
	 * @return a {@link javax.naming.NameParser NameParser} for flat namespaces.
	 */
	public static NameParser flatNameParser() {
		if (Constants.flatParser == null) {
			Properties flatSyntax = new Properties();
			flatSyntax.put("jndi.syntax.direction", "flat"); //$NON-NLS-1$ //$NON-NLS-2$
			flatSyntax.put("jndi.syntax.ignorecase", "false"); //$NON-NLS-1$ //$NON-NLS-2$
			Constants.flatParser = new DefaultParser(flatSyntax);
		}
		return Constants.flatParser;
	}
}
