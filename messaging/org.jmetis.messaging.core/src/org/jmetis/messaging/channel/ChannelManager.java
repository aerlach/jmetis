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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.jmetis.messaging.core.IChannelManager;
import org.jmetis.messaging.core.IInboundChannel;
import org.jmetis.messaging.core.MessageException;

/**
 * {@code ChannelManager}
 * 
 * @author era
 */
public abstract class ChannelManager implements IChannelManager {

	/**
	 * Constructs a new {@code ChannelManager} instance.
	 */
	protected ChannelManager() {
		super();
	}

	protected URI createTemporaryIdentifier(String scheme)
			throws URISyntaxException {
		return new URI(scheme + ":" + UUID.randomUUID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.messaging.core.IChannelManager#createTemporaryInboundChannel
	 * (java.lang.String)
	 */
	public IInboundChannel<?> createTemporaryInboundChannel(String scheme) {
		try {
			return this.getInboundChannel(this
					.createTemporaryIdentifier(scheme));
		} catch (URISyntaxException ex) {
			throw new MessageException(ex.getMessage(), ex);
		}
	}

}
