package org.jmetis.messaging.osgi;

import org.jmetis.messaging.core.IInboundChannel;
import org.jmetis.messaging.core.IMessage;
import org.jmetis.messaging.core.IOutboundChannel;
import org.jmetis.messaging.core.Message;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

/**
 * {@code MessageChannelTest}
 * 
 * @author aerlach
 */
public class MessageChannelTest {

	private ServiceTracker eventAdminTracker;

	private IOutboundChannel<String> outboundChannel;

	private IInboundChannel<String> inboundChannel;

	/**
	 * Constructs a new {@code MessageChannelTest} instance.
	 * 
	 * @param name
	 *            the name of the test method
	 */
	public MessageChannelTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		// TODO
		Primitives.ensureBundleStarted("org.eclipse.equinox.event");
		Primitives.ensureBundleStarted("org.jmetis.messaging.tests");
		Bundle bundle = Primitives.bundleNamed("org.jmetis.messaging.tests");
		BundleContext bundleContext = bundle.getBundleContext();
		this.eventAdminTracker = new ServiceTracker(bundleContext,
				EventAdmin.class.getName(), null);
		this.eventAdminTracker.open();
		String eventTopic = "org/jmetis/messaging/test";
		this.inboundChannel = new InboundEventChannel<String>(bundleContext,
				eventTopic);
		this.outboundChannel = new OutboundEventChannel<String>(bundleContext,
				this.eventAdminTracker, eventTopic);
	}

	@After
	public void tearDown() throws Exception {
		this.eventAdminTracker.close();
	}

	@Test
	public void testSendMessages() throws Exception {
		IMessage<String> requestMessage = new Message<String>();
		requestMessage.setPayload("TEST");
		this.outboundChannel.sendMessage(requestMessage);
		IMessage<?> responseMessage = this.inboundChannel
				.receiveMessage(IInboundChannel.INFINITE_TIMEOUT);
		Assert.assertNotNull(responseMessage);
		Assert.assertNotNull(responseMessage.getPayload());
		Assert.assertSame(requestMessage.getPayload(), responseMessage
				.getPayload());
	}
}
