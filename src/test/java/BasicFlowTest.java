import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;

import static org.junit.Assert.assertEquals;

public class BasicFlowTest {

    private BrokerHelper brokerHelper;
    private Session session;

    @Before
    public void init() throws JMSException {
        brokerHelper = new BrokerHelper();
        session = brokerHelper.getSession();
    }

    @After
    public void finish() throws JMSException {
        brokerHelper.finish();
    }

    @Test
    public void testTextMessege() throws JMSException {
        Queue queue = session.createQueue("HAPPY_WATER.Q");
        TextMessage expectedMessage = sendTextMessage(queue, "Sad crucian is looking for happy water");
        TextMessage actualMessage = receiveTextMessage(queue);
        assertEquals(expectedMessage.getText(), actualMessage.getText());
        assertEquals(expectedMessage.getStringProperty("crucian_header"), actualMessage.getStringProperty("crucian_header"));
    }

    private TextMessage sendTextMessage(Queue queue, String text) throws JMSException {
        TextMessage textMessage = session.createTextMessage("Sad crucian is looking for happy water");
        textMessage.setStringProperty("crucian_header", "value");
        MessageProducer producer = session.createProducer(queue);
        producer.send(textMessage);
        return textMessage;
    }

    private TextMessage receiveTextMessage(Queue queue) throws JMSException {
        MessageConsumer consumer = session.createConsumer(queue);
        // /blocking wait untill message comes
        // can wait forever if timeout not specified
        return (TextMessage) consumer.receive(1000);
    }
}
