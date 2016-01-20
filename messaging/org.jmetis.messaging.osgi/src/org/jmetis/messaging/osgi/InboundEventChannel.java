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

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jmetis.messaging.channel.InboundChannel;
import org.jmetis.messaging.core.IChannelManager;
import org.jmetis.messaging.core.IMessage;
import org.jmetis.messaging.core.IMessageListener;
import org.jmetis.messaging.core.IMessageSelector;
import org.jmetis.messaging.core.MessageException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

/**
 * {@code InboundEventChannel}
 * 
 * @author era
 */
public class InboundEventChannel<T> extends InboundChannel<T> implements
		EventHandler {

	static final String EVENT_PAYLOAD = "payload";

	private final String eventTopic;

	private final BundleContext bundleContext;

	private final ServiceRegistration handlerRegistration;

	private MutableMessageSelectorDecorator<T> messageSelector;

	private List<IMessageListener<T>> channelListeners;

	private final BlockingQueue<Event> eventQueue;

	private Thread dispatcherThread;

	/**
	 * Constructs a new {@code InboundEventChannel} instance.
	 * 
	 */
	public InboundEventChannel(BundleContext bundleContext, String eventTopic) {
		super();
		this.bundleContext = bundleContext;
		this.eventTopic = eventTopic;
		this.eventQueue = new LinkedBlockingQueue<Event>();
		Dictionary<String, String> serviceProperties = new Hashtable<String, String>();
		serviceProperties.put(EventConstants.EVENT_TOPIC, this.eventTopic);
		this.handlerRegistration = this.bundleContext.registerService(
				EventHandler.class.getName(), this, serviceProperties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.messaging.core.IMessageChannel#getChannelManager()
	 */
	public IChannelManager getChannelManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.messaging.core.IInboundChannel#getMessageSelector()
	 */
	@Override
	public IMessageSelector<T> getMessageSelector() {
		if (this.messageSelector != null) {
			return this.messageSelector.getComponent();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.messaging.core.IInboundChannel#setMessageSelector(org.jmetis
	 * .messaging.core.IMessageSelector)
	 */
	@Override
	public void setMessageSelector(IMessageSelector<T> messageSelector) {
		if (this.messageSelector == null) {
			this.messageSelector = new MutableMessageSelectorDecorator<T>(
					messageSelector);
		} else {
			this.messageSelector.setComponent(messageSelector);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.messaging.core.IInboundChannel#addChannelListener(org.jmetis
	 * .messaging.core.IChannelListener)
	 */
	@Override
	public synchronized void addMessageListener(
			IMessageListener<T> channelListener) {
		if (this.channelListeners == null) {
			this.channelListeners = new CopyOnWriteArrayList<IMessageListener<T>>();
			this.dispatcherThread = new Thread(new MessageDispatcher<T>(
					this.eventQueue, this.messageSelector,
					this.channelListeners), "Async Message Dispatcher ["
					+ this.eventTopic + "]");
			this.dispatcherThread.setDaemon(true);
			this.dispatcherThread.start();
		}
		this.channelListeners.add(channelListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.messaging.core.IInboundChannel#removeChannelListener(org.jmetis
	 * .messaging.core.IChannelListener)
	 */
	@Override
	public synchronized void removeMessageListener(
			IMessageListener<T> channelListener) {
		if (this.channelListeners != null) {
			this.channelListeners.remove(channelListener);
			if (this.channelListeners.size() == 0) {
				this.channelListeners = null;
				this.dispatcherThread.interrupt();
				this.dispatcherThread = null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.service.event.EventHandler#handleEvent(org.osgi.service.event
	 * .Event)
	 */
	public void handleEvent(Event event) {
		if (this.eventQueue != null) {
			synchronized (this.eventQueue) {
				this.eventQueue.add(event);
				this.eventQueue.notify();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.messaging.core.IInboundChannel#receiveMessage(long)
	 */
	@SuppressWarnings("unchecked")
	public IMessage<T> receiveMessage(long timeout) throws MessageException {
		IMessage<T> message = null;
		try {
			Event event = this.eventQueue.poll(timeout, TimeUnit.MILLISECONDS);
			if (event != null) {
				message = (IMessage<T>) event
						.getProperty(InboundEventChannel.EVENT_PAYLOAD);
			}
		} catch (InterruptedException ex) {
			// do nothing
		}
		return message;
	}

}
