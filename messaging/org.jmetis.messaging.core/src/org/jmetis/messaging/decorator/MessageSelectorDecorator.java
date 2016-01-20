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
package org.jmetis.messaging.decorator;

import org.jmetis.messaging.core.IMessage;
import org.jmetis.messaging.core.IMessageSelector;

/**
 * {@code MessageSelectorDecorator}
 * 
 * @author era
 */
public class MessageSelectorDecorator<T> implements IMessageSelector<T> {

	protected IMessageSelector<T> component;

	/**
	 * Constructs a new {@code MessageSelectorDecorator} instance.
	 * 
	 */
	public MessageSelectorDecorator(IMessageSelector<T> component) {
		super();
		this.component = component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.messaging.core.IMessageSelector#acceptMessage(org.jmetis.messaging
	 * .core.IMessage)
	 */
	public boolean acceptMessage(IMessage<T> message) {
		return this.component == null || this.component.acceptMessage(message);
	}

}
