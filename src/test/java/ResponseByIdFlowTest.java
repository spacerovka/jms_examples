import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;

import static org.junit.Assert.assertEquals;

public class ResponseByIdFlowTest {

    public static final String ANSWER = "ANSWER";
    public static final String QUESTION = "QUESTION";
    private BrokerHelper brokerHelper;
    private Session session;
    private Queue requestQueue;
    private Queue responseQueue;
    private MessageProducer requestor;

    @Before
    public void init() throws JMSException {
        brokerHelper = new BrokerHelper();
        session = brokerHelper.getSession();
        createRequestor();
        createResponder();
        responseQueue = session.createQueue("RESPONSE.Q");
    }

    @After
    public void finish() throws JMSException {
        brokerHelper.finish();
    }

    @Test
    public void testSendMessageAndRecieveResponse() throws JMSException {
        MapMessage request = session.createMapMessage();
        request.setJMSReplyTo(responseQueue);
        request.setString(QUESTION, "Where is crucian?");
        requestor.send(request);

        String filter = "JMSCorrelationID = '" + request.getJMSMessageID() + "'";
        MessageConsumer responseConsumer = session.createConsumer(responseQueue, filter);

        MapMessage response = (MapMessage) responseConsumer.receive(1000);
        assertEquals("Crucian is in the happy pond", response.getString(ANSWER));
    }

    private void createRequestor() throws JMSException {
        requestQueue = session.createQueue("REQUEST.Q");
        requestor = session.createProducer(requestQueue);
    }

    private void createResponder() {
        new Thread(() -> {
            try {
                MessageConsumer responder = session.createConsumer(requestQueue);
                MapMessage receivedMessage = (MapMessage) responder.receive(1000);

                MapMessage response = session.createMapMessage();
                response.setString(ANSWER, "Crucian is in the happy pond");
                response.setJMSCorrelationID(receivedMessage.getJMSMessageID());

                MessageProducer producer = session.createProducer(receivedMessage.getJMSReplyTo());
                producer.send(response);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
