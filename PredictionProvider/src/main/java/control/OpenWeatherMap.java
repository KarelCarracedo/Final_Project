package control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Location;
import model.Weather;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;


import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMap implements WeatherProvider {
    private final String apiKey;
    private final String ss = "OpenWeatherMap";
    public OpenWeatherMap(String apiKey) {
        this.apiKey = apiKey;
    }
    @Override
    public List<Weather> get(Location location) {
        String baseUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" +
                location.getLatitude() + "&lon=" + location.getLongitude() + "&units=metric&appid=" + apiKey;

        List<Weather> noonWeatherList = new ArrayList<>();

        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(baseUrl);
            HttpResponse response = httpClient.execute(request);

            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(reader, JsonObject.class);

            JsonArray forecasts = jsonResponse.getAsJsonArray("list");
            for (JsonElement forecastElement : forecasts) {
                JsonObject forecast = forecastElement.getAsJsonObject();

                String dateTimeText = forecast.get("dt_txt").getAsString();

                if (dateTimeText.endsWith("12:00:00")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    Instant instant = LocalDateTime.parse(forecast.get("dt_txt").getAsString(), formatter).toInstant(ZoneOffset.UTC);

                    double temperature = forecast.getAsJsonObject("main").get("temp").getAsDouble();
                    int humidity = forecast.getAsJsonObject("main").get("humidity").getAsInt();
                    int clouds = forecast.getAsJsonObject("clouds").get("all").getAsInt();
                    double windSpeed = forecast.getAsJsonObject("wind").get("speed").getAsDouble();
                    double precipitationProbability = forecast.get("pop").getAsDouble();
                    String ts = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
                    String predictionInstant = DateTimeFormatter.ISO_INSTANT.format(instant);

                    Weather weather = new Weather(temperature, humidity, clouds, windSpeed, precipitationProbability, location, ts, getSs(), predictionInstant);
                    noonWeatherList.add(weather);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return noonWeatherList;
    }

    public String getSs() {
        return ss;
    }
}