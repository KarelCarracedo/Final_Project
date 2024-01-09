package control;

import model.Hotel;
import model.Weather;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DbManager {
    private Connection connection;

    {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:datamart.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Weather> getWeatherDataBetweenDates(String capitalCity) {
        List<Weather> weatherList = new ArrayList<>();

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate fiveDaysLater = tomorrow.plusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String tomorrowStr = tomorrow.format(formatter);
        String fiveDaysLaterStr = fiveDaysLater.format(formatter);

        String query = "SELECT DISTINCT predictionTime, temperature," +
                " windSpeed, precipitationProbability, location FROM weather_data WHERE location = ? AND predictionTime BETWEEN ? AND ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, capitalCity);
            preparedStatement.setString(2, tomorrowStr);
            preparedStatement.setString(3, fiveDaysLaterStr);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    double temperature = resultSet.getDouble("temperature");
                    double windSpeed = resultSet.getDouble("windSpeed");
                    double precipitationProbability = resultSet.getDouble("precipitationProbability");
                    String location = resultSet.getString("location");
                    String predictionTime = resultSet.getString("predictionTime");

                    Weather weather = new Weather(temperature, windSpeed, precipitationProbability, location, predictionTime);
                    weatherList.add(weather);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return weatherList;
    }

    public List<Hotel> getHotelData(String targetIsland, String targetCapital) {
        List<Hotel> hotelList = new ArrayList<>();

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate fiveDaysLater = tomorrow.plusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String tomorrowStr = tomorrow.format(formatter);
        String fiveDaysLaterStr = fiveDaysLater.format(formatter);

        String query = "SELECT DISTINCT name, ratePerNight" +
                ", totalRate, hotelClass, overallRating" +
                ", islandName, islandCapitalCity, checkIn, checkOut FROM hotel_data WHERE islandName = ? AND islandCapitalCity = ? " +
                "AND checkIn >= ? AND checkOut <= ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, targetIsland);
            preparedStatement.setString(2, targetCapital);
            preparedStatement.setString(3, tomorrowStr);
            preparedStatement.setString(4, fiveDaysLaterStr);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String ratePerNight = resultSet.getString("ratePerNight");
                    String totalRate = resultSet.getString("totalRate");
                    String hotelClass = resultSet.getString("hotelClass");
                    String overallRating = resultSet.getString("overallRating");
                    String islandName = resultSet.getString("islandName");
                    String islandCapitalCity = resultSet.getString("islandCapitalCity");
                    String checkIn = resultSet.getString("checkIn");
                    String checkOut = resultSet.getString("checkOut");

                    Hotel hotel = new Hotel(name, ratePerNight, totalRate, hotelClass, overallRating,
                            islandName, islandCapitalCity, checkIn, checkOut);
                    hotelList.add(hotel);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotelList;
    }
}