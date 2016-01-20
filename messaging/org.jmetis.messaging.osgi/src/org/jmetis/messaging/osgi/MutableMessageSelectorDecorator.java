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

import org.jmetis.messaging.core.IMessageSelector;
import org.jmetis.messaging.decorator.MessageSelectorDecorator;

/**
 * {@code MutableMessageSelectorDecorator}
 * 
 * @author era
 */
class MutableMessageSelectorDecorator<T> extends MessageSelectorDecorator<T> {

	/**
	 * Constructs a new {@code MutableMessageSelectorDecorator} instance.
	 * 
	 * @param component
	 */
	public MutableMessageSelectorDecorator(IMessageSelector<T> component) {
		super(component);
	}

	/**
	 * @return the component
	 */
	public IMessageSelector<T> getComponent() {
		return this.component;
	}

	/**
	 * @param component
	 *            the component to set
	 */
	public void setComponent(IMessageSelector<T> component) {
		this.component = component;
	}

}
