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
package org.jmetis.messaging.adapter;

import org.jmetis.messaging.core.IMessage;
import org.jmetis.messaging.core.IOutboundChannel;
import org.jmetis.messaging.core.Message;

/**
 * {@code ProducerAdapter}
 * 
 * @author era
 */
public class ProducerAdapter<T> {

	private final IOutboundChannel<T> outboundChannel;

	/**
	 * Constructs a new {@code ProducerAdapter} instance.
	 * 
	 */
	public ProducerAdapter(IOutboundChannel<T> outboundChannel) {
		super();
		this.outboundChannel = outboundChannel;
	}

	public void sendPayload(T payload) {
		IMessage<T> message = new Message<T>(payload);
		this.outboundChannel.sendMessage(message);
	}

}
