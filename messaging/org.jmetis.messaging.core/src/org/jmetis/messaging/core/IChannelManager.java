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
package org.jmetis.messaging.core;

import java.net.URI;

/**
 * {@code IChannelManager} creates instances of @link IInboundChannel}, or
 * {@link IOutboundChannel}.
 * 
 * @author era
 */
public interface IChannelManager {

	/**
	 * Returns an {@link IInboundChannel} with the given {@link URI
	 * channelIdentifier}.
	 * 
	 * @param channelIdentifier
	 *            the {@link URI channelIdentifier}
	 * @return the {@link IInboundChannel} with the given {@link URI
	 *         channelIdentifier}
	 */
	IInboundChannel<?> getInboundChannel(URI channelIdentifier);

	/**
	 * Returns an {@link IOutboundChannel} with the given {@link URI
	 * channelIdentifier}.
	 * 
	 * @param channelIdentifier
	 *            the {@link URI channelIdentifier}
	 * @return the {@link IOutboundChannel} with the given {@link URI
	 *         channelIdentifier}
	 */
	IOutboundChannel<?> getOutboundChannel(URI channelIdentifier);

	/**
	 * Creates an temporary {@link IOutboundChannel}.
	 * 
	 * @param scheme
	 *            TODO
	 * 
	 * @return the temporary {@link IOutboundChannel}
	 */
	IInboundChannel<?> createTemporaryInboundChannel(String scheme);

}
