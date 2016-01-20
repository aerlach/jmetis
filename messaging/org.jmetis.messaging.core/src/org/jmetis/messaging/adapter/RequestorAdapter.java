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
import org.jmetis.messaging.core.IOutboundChannel;
import org.jmetis.messaging.core.Message;

/**
 * {@code ConsumerAdapter}
 * 
 * @author era
 */
public class RequestorAdapter<I, O> {

	private final IOutboundChannel<O> outboundChannel;

	private IInboundChannel<I> inboundChannel;

	private long timeout;

	/**
	 * Constructs a new {@code ConsumerAdapter} instance.
	 * 
	 */
	public RequestorAdapter(IOutboundChannel<O> outboundChannel, long timeout) {
		super();
		this.outboundChannel = outboundChannel;
		this.timeout = timeout;

	}

	public RequestorAdapter(IOutboundChannel<O> outboundChannel) {
		this(outboundChannel, IInboundChannel.INFINITE_TIMEOUT);
	}

	public I sendAndReceive(O request) {
		IMessage<O> requestMessage = new Message<O>(request);
		this.outboundChannel.sendMessage(requestMessage);
		I payload = null;
		IMessage<I> replyMessage = this.inboundChannel
				.receiveMessage(this.timeout);
		if (replyMessage != null) {
			payload = replyMessage.getPayload();
		}
		return payload;
	}

}
