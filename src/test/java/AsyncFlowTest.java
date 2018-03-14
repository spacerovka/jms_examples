import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AsyncFlowTest {

    private BrokerHelper brokerHelper;
    private Session session;
    private Queue queue;

    @Before
    public void init() throws JMSException {
        brokerHelper = new BrokerHelper();
        session = brokerHelper.getSession();
        queue = session.createQueue("ASYNC_QUEUE");
    }

    @After
    public void finish() throws JMSException {
        brokerHelper.finish();
    }

    @Test
    public void testTextMessege() throws JMSException {
        final List<String> result = new ArrayList<String>();
        new Thread(() -> new AsyncConsumer(session, queue, result)).start();

        sendTextMessage(queue, "One");
        sendTextMessage(queue, "Two");

        assertEquals(2, result.size());
    }

    private TextMessage sendTextMessage(Queue queue, String text) throws JMSException {
        TextMessage textMessage = session.createTextMessage("Sad crucian is looking for happy water");
        MessageProducer producer = session.createProducer(queue);
        producer.send(textMessage);
        return textMessage;
    }
}
