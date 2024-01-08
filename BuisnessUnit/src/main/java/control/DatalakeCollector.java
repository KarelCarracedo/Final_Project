package control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatalakeCollector {

    private String datalakePath;

    public DatalakeCollector(String datalakePath) {
        this.datalakePath = datalakePath;
    }

    public void collect() {
        String weatherEventsPath = getDatalakePath() + "datalake/eventstore/prediction.Weather/OpenWeatherMap";
        String hotelEventsPath = getDatalakePath() + "datalake/eventstore/hotel.Information/SerpApi";

        processWeatherEventFiles(weatherEventsPath);
        processHotelEventFiles(hotelEventsPath);
    }

    private void processWeatherEventFiles(String eventsPath) {
        File eventsFolder = new File(eventsPath);

        if (!eventsFolder.exists() || !eventsFolder.isDirectory()) {
            System.out.println("Path provided is not valid: " + eventsPath);
            return;
        }

        File[] eventFiles = eventsFolder.listFiles((dir, name) -> name.endsWith(".events"));

        if (eventFiles != null) {
            for (File eventFile : eventFiles) {
                try {
                    JsonObject jsonObject = readJsonFromFile(eventFile);
                    System.out.println(jsonObject);

                    try (Connection dbConnection = connection()) {
                        createWeatherTable(dbConnection);
                        insertWeatherData(dbConnection, jsonObject.toString());
                    } catch (SQLException e) {
                        System.err.println("Error connecting to the database: " + e.getMessage());
                    }
                } catch (IOException | JsonSyntaxException e) {
                    System.err.println("Error reading the file " + eventFile.getName() + ": " + e.getMessage());
                }
            }
        }
    }

    private void processHotelEventFiles(String eventsPath) {
        File eventsFolder = new File(eventsPath);

        if (!eventsFolder.exists() || !eventsFolder.isDirectory()) {
            System.out.println("Path provided is not valid: " + eventsPath);
            return;
        }

        File[] eventFiles = eventsFolder.listFiles((dir, name) -> name.endsWith(".events"));

        if (eventFiles != null) {
            for (File eventFile : eventFiles) {
                try {
                    JsonObject jsonObject = readJsonFromFile(eventFile);
                    System.out.println(jsonObject);

                    try (Connection dbConnection = connection()) {
                        createHotelTable(dbConnection);
                        insertHotelData(dbConnection, jsonObject.toString());
                    } catch (SQLException e) {
                        System.err.println("Error connecting to the database: " + e.getMessage());
                    }
                } catch (IOException | JsonSyntaxException e) {
                    System.err.println("Error reading the file " + eventFile.getName() + ": " + e.getMessage());
                }
            }
        }
    }

    private JsonObject readJsonFromFile(File file) throws IOException, JsonSyntaxException {
        try (FileReader fileReader = new FileReader(file);
             JsonReader jsonReader = new JsonReader(fileReader)) {

            jsonReader.setLenient(true);  // Configurando para ser indulgente con JSON malformado
            Gson gson = new Gson();
            return gson.fromJson(jsonReader, JsonObject.class);
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

    public String getDatalakePath() {
        return datalakePath;
    }
}