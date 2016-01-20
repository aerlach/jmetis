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
package org.jmetis.messaging.channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jmetis.messaging.core.IInboundChannel;
import org.jmetis.messaging.core.IMessageListener;
import org.jmetis.messaging.core.IMessageSelector;

/**
 * {@code InboundChannel}
 * 
 * @author era
 */
public abstract class InboundChannel<T> extends MessageChannel<T> implements
		IInboundChannel<T> {

	private static ExecutorService EXECUTOR_SERVICE;

	/**
	 * Constructs a new {@code InboundChannel} instance.
	 */
	protected InboundChannel() {
		super();
	}

	// TODO ADD ABSTRACT DISPATCHER METHOD?
	protected void startDispatcher(Runnable dispatcher) {
		if (InboundChannel.EXECUTOR_SERVICE == null) {
			InboundChannel.EXECUTOR_SERVICE = Executors.newCachedThreadPool();
		}
		InboundChannel.EXECUTOR_SERVICE.execute(dispatcher);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.messaging.core.IInboundChannel#getMessageSelector()
	 */
	public IMessageSelector<T> getMessageSelector() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.messaging.core.IInboundChannel#setMessageSelector(org.jmetis
	 * .messaging.core.IMessageSelector)
	 */
	public void setMessageSelector(IMessageSelector<T> messageSelector) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.messaging.core.IInboundChannel#addMessageListener(org.jmetis
	 * .messaging.core.IMessageListener)
	 */
	public void addMessageListener(IMessageListener<T> channelListener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.messaging.core.IInboundChannel#removeMessageListener(org.jmetis
	 * .messaging.core.IMessageListener)
	 */
	public void removeMessageListener(IMessageListener<T> channelListener) {
		// TODO Auto-generated method stub

	}

}
