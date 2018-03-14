import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class AsyncConsumer implements MessageListener {

    private List<String> messages;

    public AsyncConsumer(Session session, Queue queue, List<String> storage) {
        try {
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(this);
            messages = storage;
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void onMessage(Message message) {
        try {
            TextMessage received = (TextMessage)message;
            messages.add(received.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
