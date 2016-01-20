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
package org.jmetis.collections.decorator;

import java.util.Set;

/**
 * {@code SetDecorator} decorates another {@link Set} to provide additional
 * behavior.
 * 
 * @author era
 */
public class SetDecorator<E> extends CollectionDecorator<Set<E>, E> implements
		Set<E> {

	/**
	 * Constructs a new {@code SetDecorator} instance that wraps (not copies)
	 * the given {@code component}.
	 * 
	 * @param component
	 *            the {@link Set} to decorate, must not be {@code null}
	 * @throws NullPointerException
	 *             if the given {@code component} is {@code null}
	 */
	public SetDecorator(Set<E> component) {
		super(component);
	}

}
