import javax.jms.JMSException;
import javax.jms.Session;

public class JmsExampleApplication {


    private static BrokerHelper brokerHelper;
    private static Session session;

    public static void main(String[] args) throws JMSException {
        brokerHelper = new BrokerHelper();
        session = brokerHelper.getSession();
        brokerHelper.finish();
    }


}
