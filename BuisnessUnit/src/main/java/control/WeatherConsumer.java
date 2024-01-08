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

public class WeatherConsumer {
    public void weatherConsumeBroker() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            javax.jms.Connection connection = connectionFactory.createConnection();
            connection.setClientID("BusinessUnitWeather");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic destination = session.createTopic("prediction.Weather");
            MessageConsumer subscriber = session.createDurableSubscriber(destination, "BusinessUnitWeather");
            Connection connectiondb = connection();
            createWeatherTable(connectiondb);
            subscriber.setMessageListener(message -> {
                String jsonText = null;
                try {
                    jsonText = ((TextMessage) message).getText();
                    insertWeatherData(connectiondb, jsonText);
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

    private void createWeatherTable(java.sql.Connection connection) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS weather_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "temperature REAL," +
                "windSpeed REAL," +
                "precipitationProbability INTEGER," +
                "location TEXT," +
                "predictionTime TEXT)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertWeatherData(Connection connection, String jsonText) {
        String insertSQL = "INSERT OR REPLACE INTO weather_data " +
                "(temperature, windSpeed, precipitationProbability, location, predictionTime) " +
                "VALUES (?, ?, ?, ?, ?)";
        Gson gson = new Gson();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            JsonObject jsonObject = gson.fromJson(jsonText, JsonObject.class);
            preparedStatement.setDouble(1, jsonObject.get("temperature").getAsDouble());
            preparedStatement.setDouble(2, jsonObject.get("windSpeed").getAsDouble());
            preparedStatement.setInt(3, jsonObject.get("precipitationProbability").getAsInt());
            preparedStatement.setString(4, jsonObject.getAsJsonObject("location").get("locationName").getAsString());
            preparedStatement.setString(5, jsonObject.get("predictionTime").getAsString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}