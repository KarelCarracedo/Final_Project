import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Receiver {
    public void gsonReceiver() throws JMSException {
        Gson gson = new Gson();
        String url = ActiveMQConnection.DEFAULT_BROKER_URL;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("Luis");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic destination = session.createTopic("prediction.Weather");

        MessageConsumer consumer = session.createDurableSubscriber(destination, "Luis");

        try {
            String baseDirectory = "eventstore/prediction.Weather";

            File baseDir = new File(baseDirectory);
            if (!baseDir.exists() && !baseDir.mkdirs()) {
                throw new RuntimeException("No se pudo crear el directorio base: " + baseDirectory);
            }

            consumer.setMessageListener(message -> {
                try {
                    String jsonText = ((TextMessage) message).getText();
                    System.out.println(jsonText);

                    JsonObject jsonObject = gson.fromJson(jsonText, JsonObject.class);

                    String ssValue = jsonObject.get("ss").getAsString();
                    String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

                    String ssDirectory = baseDirectory + "/" + ssValue;

                    File ssDir = new File(ssDirectory);
                    if (!ssDir.exists() && !ssDir.mkdirs()) {
                        throw new RuntimeException("No se pudo crear el directorio: " + ssDirectory);
                    }

                    String fileName = ssDirectory + "/" + currentDate + ".events";

                    try (FileWriter fileWriter = new FileWriter(fileName, true);
                         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                        bufferedWriter.write(jsonObject.toString());
                        bufferedWriter.newLine(); // Agregar un salto de línea entre eventos
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            });

            System.out.println("Presiona Enter para salir.");
            System.in.read();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}