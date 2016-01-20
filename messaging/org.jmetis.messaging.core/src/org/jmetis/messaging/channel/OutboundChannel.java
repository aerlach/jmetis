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

import org.jmetis.messaging.core.IOutboundChannel;

/**
 * {@code OutboundChannel}
 * 
 * @author era
 */
public abstract class OutboundChannel<T> extends MessageChannel<T> implements
		IOutboundChannel<T> {

	private int priority;

	private long timeToLive;

	/**
	 * Constructs a new {@code OutboundChannel} instance.
	 */
	protected OutboundChannel() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.messaging.core.IOutboundChannel#getPriority()
	 */
	public int getPriority() {
		return this.priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.messaging.core.IOutboundChannel#setPriority(int)
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.messaging.core.IOutboundChannel#getTimeToLive()
	 */
	public long getTimeToLive() {
		return this.timeToLive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.messaging.core.IOutboundChannel#setTimeToLive(long)
	 */
	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

}
