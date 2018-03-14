import model.Crucian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;

import static org.junit.Assert.assertEquals;

public class PublisherSubscriberFlow {
    public static final String TOPIC_NAME = "CRUCIAN.T";
    private BrokerHelper brokerHelper;
    private Session session;
    Topic topic;
    MessageProducer publisher;
    private TopicSubscriber subscriber;

    @Before
    public void init() throws JMSException {
        brokerHelper = new BrokerHelper();
        session = brokerHelper.getSession();
        createTopic();
        createPublisher();
    }

    @After
    public void finish() throws JMSException {
        brokerHelper.finish();
    }

    @Test
    public void testPublishSubscribeFlow() throws JMSException {
        sendMessageBeforeSubscriberExists();
        createDurableSubscriber();

        ObjectMessage recieved = (ObjectMessage) subscriber.receive(1000);
        Crucian crucianInPond = (Crucian) recieved.getObject();

        assertEquals("Borys", crucianInPond.getName());
        assertEquals("Smiling", crucianInPond.getMood());
    }

    private void sendMessageBeforeSubscriberExists() throws JMSException {
        ObjectMessage message = session.createObjectMessage(new Crucian("Borys", "Smiling"));
        publisher.send(message);
    }

    private void createPublisher() throws JMSException {
        publisher =  session.createProducer(topic);
    }

    private void createTopic() throws JMSException {
        topic = session.createTopic(TOPIC_NAME);
    }

    private void createDurableSubscriber() throws JMSException {
        subscriber = session.createDurableSubscriber(topic, "Pond");
    }
}
