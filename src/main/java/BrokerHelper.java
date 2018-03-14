import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.UUID;

public class BrokerHelper {

    private static final boolean TRANSACTED_SESSION = false;
    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;

    public BrokerHelper() throws JMSException {
        createConnectionFactory();
        createConnection();
        createSession();
    }

    public void finish() throws JMSException {
        connection.close(); //internaly close the session
    }

    private void createConnectionFactory() throws JMSException {
        connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        //need for durable subscribers
        connectionFactory.setClientID("crucian_pond");
    }

    private void createConnection() throws JMSException {
        connection = connectionFactory.createConnection();
        connection.start();
    }

    private void createSession() throws JMSException {
        session = connection.createSession(TRANSACTED_SESSION, Session.AUTO_ACKNOWLEDGE);
    }

    public Session getSession() {
        return session;
    }
}
