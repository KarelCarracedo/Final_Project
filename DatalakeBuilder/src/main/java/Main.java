import javax.jms.JMSException;

public class Main {
    public static void main(String[] args) throws JMSException {
        Receiver receiver = new Receiver();
        receiver.gsonReceiver();
    }
}
