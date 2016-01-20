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

import org.jmetis.messaging.core.IInboundChannel;
import org.jmetis.messaging.core.IMessage;

/**
 * {@code ConsumerAdapter}
 * 
 * @author era
 */
public class ConsumerAdapter<T> {

	private final IInboundChannel<T> inboundChannel;

	private long timeout;

	/**
	 * Constructs a new {@code ConsumerAdapter} instance.
	 * 
	 */
	public ConsumerAdapter(IInboundChannel<T> inboundChannel, long timeout) {
		super();
		this.inboundChannel = inboundChannel;
		this.timeout = timeout;

	}

	public ConsumerAdapter(IInboundChannel<T> inboundChannel) {
		this(inboundChannel, IInboundChannel.INFINITE_TIMEOUT);
	}

	public T receivePayload() {
		T payload = null;
		IMessage<T> message = this.inboundChannel.receiveMessage(this.timeout);
		if (message != null) {
			payload = message.getPayload();
		}
		return payload;
	}

}
