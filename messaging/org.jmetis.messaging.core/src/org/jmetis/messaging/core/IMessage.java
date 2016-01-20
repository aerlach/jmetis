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
import java.util.Date;

/**
 * {@code IMessage}
 * 
 * @author era
 * 
 * @param <T>
 */
public interface IMessage<T> {

	/**
	 * {@code Priority} defines the possible values for a message's priority.
	 */
	enum Priority {
		LOW, NORMAL, HIGH
	}

	/**
	 * Returns the message ID.
	 * 
	 * @return the message ID
	 */
	Object getId();

	/**
	 * Returns the correlation ID for the message.
	 * 
	 * @return the correlation ID for the message
	 */
	Object getCorrelationId();

	/**
	 * Sets the correlation ID for the message.
	 * 
	 * @param correlationId
	 *            the correlation ID for the message
	 */
	void setCorrelationId(Object correlationId);

	/**
	 * Returns the expiration date of the message.
	 * 
	 * @return the expiration date of the message
	 */
	Date getExpirationDate();

	/**
	 * Sets the expiration date for the message.
	 * 
	 * @param expirationDate
	 *            the expiration date for the message
	 */
	void setExpirationDate(Date expirationDate);

	/**
	 * Returns the priority of the message.
	 * 
	 * @return the priority of the message.
	 * 
	 * @see IMessage.Priority
	 */
	Priority getPriority();

	/**
	 * Sets the priority for the message.
	 * 
	 * @param priority
	 *            the priority for the message
	 */
	void setPriority(Priority priority);

	/**
	 * Returns the sequence number for this message in the the correlation group
	 * (as defined by the correlationId), or -1 if the sequence is not
	 * important.
	 * 
	 * @param sequenceNumber
	 *            the sequence number for this message in the the correlation
	 *            group
	 */
	int getSequenceNumber();

	/**
	 * Sets the sequence number for this message in the the correlation group
	 * (as defined by the correlationId).
	 * 
	 * @param sequenceNumber
	 *            the sequence number for this message in the the correlation
	 *            group
	 */
	void setSequenceNumber(int sequenceNumber);

	/**
	 * Returns the number of messages are in the correlation group, or -1 if the
	 * size is not known.
	 * 
	 * @return the number of messages are in the correlation group
	 */
	int getSequenceSize();

	/**
	 * Sets the number of messages are in the correlation group, or -1 if the
	 * size is not known.
	 * 
	 * @param sequenceSize
	 *            the number of messages are in the correlation group
	 */
	void setSequenceSize(int sequenceSize);

	/**
	 * Returns the channel to which a reply to this message should be sent.
	 * 
	 * @return the channel to which a reply to this message should be sent
	 */
	URI getReplyChannel();

	/**
	 * Sets the channel to which a reply to this message should be sent.
	 * 
	 * @param replyChannel
	 *            the channel to which a reply to this message should be sent
	 */
	void setReplyChannel(URI replyChannel);

	/**
	 * Returns the payload of the message.
	 * 
	 * @return the payload of the message
	 */
	T getPayload();

	/**
	 * Sets the payload the message.
	 * 
	 * @param payload
	 *            the payload the message
	 */
	void setPayload(T payload);

	/**
	 * Returns property names of the receiver.
	 * 
	 * @return a non-empty array with one element per property.
	 */
	String[] getPropertyNames();

}
