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

/**
 * {@code IOutboundChannel} is used to send messages to a message channel.
 * 
 * @author era
 */
public interface IOutboundChannel<T> extends IMessageChannel<T> {

	/**
	 * Returns the producer's default priority.
	 * 
	 * @return the message priority for this message producer
	 */
	int getPriority();

	/**
	 * Sets the producer's default priority.
	 * 
	 * @param defaultPriority
	 *            the message priority for this message producer; must be a
	 *            value between 0 and 9
	 */
	void setPriority(int defaultPriority);

	/**
	 * Returns the default length of time in milliseconds from its dispatch time
	 * that a produced message should be retained by the message system.
	 * 
	 * @return the message time to live in milliseconds; zero is unlimited
	 */
	long getTimeToLive();

	/**
	 * /** Sets the default length of time in milliseconds from its dispatch
	 * time that a produced message should be retained by the message system.
	 * 
	 * <p>
	 * Time to live is set to zero by default.
	 * 
	 * @param timeToLive
	 *            the message time to live in milliseconds; zero is unlimited
	 */
	void setTimeToLive(long timeToLive);

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            the message to send
	 * @throws MessageException
	 */
	void sendMessage(IMessage<T> message) throws MessageException;

}
