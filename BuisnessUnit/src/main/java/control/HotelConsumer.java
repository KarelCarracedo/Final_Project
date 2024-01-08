package control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HotelConsumer {
    public void hotelConsumeBroker() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            javax.jms.Connection connection = connectionFactory.createConnection();
            connection.setClientID("BusinessUnitHotel");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic destination = session.createTopic("hotel.Information");
            MessageConsumer subscriber = session.createDurableSubscriber(destination, "BusinessUnitHotel");
            Connection connectiondb = connection();
            createHotelTable(connectiondb);
            subscriber.setMessageListener(message -> {
                String jsonText = null;
                try {
                    jsonText = ((TextMessage) message).getText();
                    System.out.println(jsonText); //TODO Borrar
                    insertHotelData(connectiondb, jsonText);
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection connection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:datamart.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    private void createHotelTable(Connection connection) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS hotel_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "ratePerNight TEXT," +
                "totalRate TEXT," +
                "hotelClass TEXT," +
                "overallRating TEXT," +
                "islandName TEXT," +
                "islandCapitalCity TEXT," +
                "checkIn TEXT," +
                "checkOut TEXT)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertHotelData(Connection connection, String jsonText) {
        String insertOrUpdateSQL = "INSERT OR REPLACE INTO hotel_data " +
                "(name, ratePerNight, totalRate, hotelClass, overallRating, islandName, islandCapitalCity, checkIn, checkOut) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Gson gson = new Gson();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrUpdateSQL)) {
            JsonObject jsonObject = gson.fromJson(jsonText, JsonObject.class);
            preparedStatement.setString(1, jsonObject.get("name").getAsString());
            preparedStatement.setString(2, jsonObject.get("ratePerNight").getAsString());
            preparedStatement.setString(3, jsonObject.get("totalRate").getAsString());
            preparedStatement.setString(4, jsonObject.get("hotelClass").getAsString());
            preparedStatement.setString(5, jsonObject.get("overallRating").getAsString());
            preparedStatement.setString(6, jsonObject.getAsJsonObject("island").get("name").getAsString());
            preparedStatement.setString(7, jsonObject.getAsJsonObject("island").get("capitalCity").getAsString());
            preparedStatement.setString(8, jsonObject.get("checkIn").getAsString());
            preparedStatement.setString(9, jsonObject.get("checkOut").getAsString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
