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

import java.util.List;
import java.util.Queue;

import org.jmetis.messaging.core.IMessage;
import org.jmetis.messaging.core.IMessageListener;
import org.jmetis.messaging.core.IMessageSelector;
import org.jmetis.messaging.core.MessageException;
import org.osgi.service.event.Event;

/**
 * {@code MessageDispatcher}
 * 
 * @author era
 */
class MessageDispatcher<T> implements Runnable {

	private final Queue<Event> eventQueue;

	private final IMessageSelector<T> messageSelector;

	private final List<IMessageListener<T>> channelListeners;

	/**
	 * Constructs a new {@code MessageDispatcher} instance.
	 * 
	 */
	public MessageDispatcher(Queue<Event> eventQueue,
			IMessageSelector<T> messageSelector,
			List<IMessageListener<T>> channelListeners) {
		super();
		this.eventQueue = eventQueue;
		this.messageSelector = messageSelector;
		this.channelListeners = channelListeners;
	}

	@SuppressWarnings("unchecked")
	protected void processEvent(Event event) {
		IMessage<T> message = (IMessage<T>) event
				.getProperty(InboundEventChannel.EVENT_PAYLOAD);
		if (this.channelListeners != null
				&& (this.messageSelector == null || this.messageSelector
						.acceptMessage(message))) {
			for (IMessageListener<T> c : this.channelListeners) {
				try {
					c.handleMessage(message);
				} catch (MessageException ex) {
					// TODO
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			while (!Thread.interrupted()) {
				while (!this.eventQueue.isEmpty()) {
					this.processEvent(this.eventQueue.remove());
				}
				synchronized (this.eventQueue) {
					this.eventQueue.wait();
				}
			}
		} catch (InterruptedException ex) {
			while (!this.eventQueue.isEmpty()) {
				this.processEvent(this.eventQueue.remove());
			}
		}
	}

}
