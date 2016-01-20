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
 * {@code IInboundChannel} is used to receive a {@link IMessage message} from a
 * message channel. {@code IInboundChannel} can be created with a message
 * selector. A message selector allows the client to restrict the messages
 * delivered to the message consumer to those that match the selector.
 * <p>
 * {@code IInboundChannel} may either synchronously receive a message consumer's
 * messages or have the consumer asynchronously deliver them as they arrive.
 * <p>
 * For synchronous receipt, a client can request the next message from a message
 * consumer using one of its {@code receive} methods. There are several
 * variations of {@code receive} that allow a client to poll or wait for the
 * next message.
 * <p>
 * For asynchronous delivery, a client can register a {@code IMessageListener}
 * object with a message consumer. As messages arrive at the message consumer,
 * it delivers them by calling the {@code IMessageListener#handleMessage}
 * method.
 * 
 * @author era
 */
public interface IInboundChannel<T> extends IMessageChannel<T> {

	long INFINITE_TIMEOUT = Long.MAX_VALUE;

	long NO_WAIT_TIMEOUT = 0;

	/**
	 * Returns the message selector of the this message channel.
	 * 
	 * @return the message selector of the this message channel, or null if no
	 *         message selector exists
	 */
	IMessageSelector<T> getMessageSelector();

	/**
	 * 
	 * @param messageSelector
	 */
	void setMessageSelector(IMessageSelector<T> messageSelector);

	/**
	 * 
	 * @param channelListener
	 */
	void addMessageListener(IMessageListener<T> channelListener);

	/**
	 * 
	 * @param channelListener
	 */
	void removeMessageListener(IMessageListener<T> channelListener);

	/**
	 * Receives the next message that arrives within the specified timeout
	 * interval.
	 * 
	 * <p>
	 * This call blocks until a message arrives, the timeout expires, or this
	 * message consumer is closed. A <CODE>timeout</CODE> of zero never expires,
	 * and the call blocks indefinitely.
	 * 
	 * @param timeout
	 *            the timeout value (in milliseconds)
	 * @return the next message produced for this message consumer, or null if
	 *         the timeout expires or this message consumer is concurrently
	 *         closed
	 * @throws MessageException
	 * @see IInboundChannel.INFINITE_TIMEOUT
	 * @see IInboundChannel.NO_WAIT_TIMEOUT
	 */
	IMessage<T> receiveMessage(long timeout) throws MessageException;

}
