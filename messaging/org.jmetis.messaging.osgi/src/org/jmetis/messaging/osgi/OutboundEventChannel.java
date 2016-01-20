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

import org.jmetis.messaging.channel.OutboundChannel;
import org.jmetis.messaging.core.IChannelManager;
import org.jmetis.messaging.core.IMessage;
import org.jmetis.messaging.core.MessageException;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

/**
 * {@code OutboundEventChannel}
 * 
 * @author era
 */
public class OutboundEventChannel<T> extends OutboundChannel<T> {

	private static final String EVENT_PAYLOAD = "payload";

	private final ServiceTracker eventAdminTracker;

	private final String eventTopic;

	/**
	 * Constructs a new {@code OutboundEventChannel} instance.
	 * 
	 */
	public OutboundEventChannel(BundleContext bundleContext,
			ServiceTracker eventAdminTracker, String eventTopic) {
		super();
		this.eventAdminTracker = eventAdminTracker;
		this.eventTopic = eventTopic;
	}

	protected EventAdmin eventAdmin() {
		EventAdmin eventAdmin = (EventAdmin) this.eventAdminTracker
				.getService();
		if (eventAdmin == null) {
			MessageException.channelNotAvailable(this.eventTopic);
		}
		return eventAdmin;
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
	 * @see
	 * org.jmetis.messaging.core.IOutboundChannel#sendMessage(org.jmetis.messaging
	 * .core.IMessage)
	 */
	public void sendMessage(IMessage<T> message) throws MessageException {
		Dictionary<String, Object> eventProperties = new Hashtable<String, Object>();
		eventProperties.put(OutboundEventChannel.EVENT_PAYLOAD, message);
		this.eventAdmin()
				.postEvent(new Event(this.eventTopic, eventProperties));
	}

}
