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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jmetis.kernel.assertion.Assertions;

/**
 * {@code Message}
 * 
 * @author aerlach
 */
public class Message<T> implements IMessage<T>, Serializable {

	private static final long serialVersionUID = 1104965320903865110L;

	private static final Map<Class<?>, Map<String, PropertyDescriptor>> CACHED_PROPERTY_DESCRIPTORS = new ConcurrentHashMap<Class<?>, Map<String, PropertyDescriptor>>();

	private transient Map<String, PropertyDescriptor> propertyDescriptors;

	private Object id;

	private Object correlationId;

	private long expirationDate;

	private Priority priority;

	private int sequenceNumber;

	private int sequenceSize;

	private URI replyChannel;

	private T payload;

	/**
	 * Constructs a new {@code Message} instance.
	 */
	public Message() {
		super();
		this.id = UUID.randomUUID();
		this.expirationDate = Long.MAX_VALUE;
		this.priority = Priority.NORMAL;
	}

	/**
	 * Constructs a new {@code Message} instance with the given {@code payload}.
	 */
	public Message(T payload) {
		this();
		this.payload = payload;
	}

	public Object getId() {
		return this.id;
	}

	public Object getCorrelationId() {
		return this.correlationId;
	}

	public void setCorrelationId(Object correlationId) {
		this.correlationId = correlationId;
	}

	public Date getExpirationDate() {
		return new Date(this.expirationDate);
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate.getTime();
	}

	public Priority getPriority() {
		return this.priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public int getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getSequenceSize() {
		return this.sequenceSize;
	}

	public void setSequenceSize(int sequenceSize) {
		this.sequenceSize = sequenceSize;
	}

	public URI getReplyChannel() {
		return this.replyChannel;
	}

	public void setReplyChannel(URI replyChannel) {
		this.replyChannel = replyChannel;
	}

	public T getPayload() {
		return this.payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

	protected Map<String, PropertyDescriptor> createPropertyDescriptors() {
		Map<String, PropertyDescriptor> propertyDescriptors = Message.CACHED_PROPERTY_DESCRIPTORS
				.get(this.getClass());
		if (propertyDescriptors == null) {
			try {
				BeanInfo beanInfo;
				if (this.getClass() == Message.class) {
					beanInfo = Introspector.getBeanInfo(this.getClass(),
							Object.class);
				} else {
					beanInfo = Introspector.getBeanInfo(this.getClass(),
							Message.class);
				}
				propertyDescriptors = new HashMap<String, PropertyDescriptor>();
				for (PropertyDescriptor propertyDescriptor : beanInfo
						.getPropertyDescriptors()) {
					propertyDescriptors.put(propertyDescriptor.getName(),
							propertyDescriptor);
				}
			} catch (IntrospectionException ex) {
				Assertions.mustNotBeReached(ex);
			}
		}
		return propertyDescriptors;
	}

	protected Map<String, PropertyDescriptor> propertyDescriptors() {
		if (this.propertyDescriptors == null) {
			this.propertyDescriptors = this.createPropertyDescriptors();
		}
		return this.propertyDescriptors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.messaging.core.IMessage#getPropertyNames()
	 */
	public String[] getPropertyNames() {
		Map<String, PropertyDescriptor> propertyDescriptors = this
				.propertyDescriptors();
		return propertyDescriptors.keySet().toArray(
				new String[propertyDescriptors.size()]);
	}

	/**
	 * Indicates whether a property value exists.
	 * 
	 * @param name
	 *            the name of the property to test
	 * @return {@code true} if the property exists; {@code false} otherwise
	 */
	public boolean containsProperty(String name) {
		return this.propertyDescriptors().containsKey(name);
	}

	protected void assertIsValidPropertyName(String name) {
		if (name == null || name.length() == 0) {
			MessageException.propertyNameNullOrEmpty();
		}
	}

	protected Object handlePropertyNotFound(String name) {
		MessageException.propertyNotFound(name);
		return null;
	}

	protected Object handleReadPropertyFailed(String name, Exception exception) {
		MessageException.readPropertyFailed(name, exception);
		return null;
	}

	protected Object handleCannotReadProperty(String name) {
		MessageException.cannotReadProperty(name);
		return null;
	}

	/**
	 * Retrieves a property.
	 * 
	 * @param name
	 *            the name of the property to retrieve
	 * 
	 * @return the value of the property, or {@code null} if not found.
	 */
	public Object getProperty(String name) {
		this.assertIsValidPropertyName(name);
		PropertyDescriptor propertyDescriptor = this.propertyDescriptors().get(
				name);
		if (propertyDescriptor != null) {
			Method readMethod = propertyDescriptor.getReadMethod();
			if (readMethod != null) {
				try {
					return readMethod.invoke(this);
				} catch (Exception ex) {
					return this.handleReadPropertyFailed(name, ex);
				}
			}
			return this.handleCannotReadProperty(name);
		}
		return this.handlePropertyNotFound(name);
	}

	protected <V> V convertValueTo(Object value, Class<V> type) {
		throw new ClassCastException();
	}

	@SuppressWarnings("unchecked")
	public <V> V getProperty(String name, Class<V> type) {
		Object value = this.getProperty(name);
		if (!type.isInstance(value)) {
			return this.convertValueTo(value, type);
		}
		return (V) value;
	}

	protected void handleWritePropertyFailed(String name, Object value,
			Exception exception) {
		MessageException.writePropertyFailed(name, exception);
	}

	protected void handleCannotWriteProperty(String name) {
		MessageException.cannotWriteProperty(name);
	}

	public void setProperty(String name, Object value) {
		this.assertIsValidPropertyName(name);
		PropertyDescriptor propertyDescriptor = this.propertyDescriptors().get(
				name);
		if (propertyDescriptor != null) {
			Method writeMethod = propertyDescriptor.getWriteMethod();
			if (writeMethod != null) {
				try {
					writeMethod.invoke(this, value);
				} catch (Exception ex) {
					this.handleWritePropertyFailed(name, value, ex);
				}
			} else {
				this.handleCannotWriteProperty(name);
			}
		} else {
			this.handlePropertyNotFound(name);
		}
	}

}
