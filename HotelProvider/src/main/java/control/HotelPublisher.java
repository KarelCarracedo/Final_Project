package control;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import model.Hotel;
import javax.jms.*;

public class HotelPublisher {
    public void hotelPublish(Hotel hotel) throws MyException {
        String url = ActiveMQConnection.DEFAULT_BROKER_URL;
        String subject = "hotel.Information";
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createTopic(subject);

            MessageProducer producer = session.createProducer(destination);

            Gson gson = new Gson();
            TextMessage message = session
                    .createTextMessage(gson.toJson(hotel));

            producer.send(message);

            connection.close();
        } catch (JMSException e) {
            throw new MyException("Error");
        }
    }
}
