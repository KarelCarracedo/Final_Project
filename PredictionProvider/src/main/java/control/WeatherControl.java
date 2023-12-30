package control;
import model.Location;
import model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherControl {
    private WeatherProvider weatherProvider;
    private Publisher publisher;
    private String apiKey;
    public WeatherControl(String apiKey) {
        this.weatherProvider = new OpenWeatherMap(apiKey);
        this.publisher = new Publisher();
    }

    public void fetchAndSaveWeather() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable weatherTask = () -> {
            List<Location> locations = readLocationsFromCSV("Localizaciones.csv");
            for (Location location : locations) {
                List<Weather> weathers = getWeatherProvider().get(location);
                for (Weather weather : weathers) {
                    try {
                        publisher.weatherPublish(weather);
                    } catch (MyException e) {
                        System.out.println("Error");
                    }
                }
            }
        };
        scheduler.scheduleAtFixedRate(weatherTask, 0, 6, TimeUnit.HOURS);
    }

    private static List<Location> readLocationsFromCSV(String csvFile) {
        List<Location> locations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] campos = line.split(",");

                String locationName = campos[0];
                double latitude = Double.parseDouble(campos[1]);
                double longitude = Double.parseDouble(campos[2]);

                Location location = new Location(latitude, longitude, locationName);
                locations.add(location);
            }

            for (Location location : locations) {
                System.out.println("Nombre: " + location.getLocationName() +
                        ", Latitud: " + location.getLatitude() +
                        ", Longitud: " + location.getLongitude());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return locations;
    }

    public WeatherProvider getWeatherProvider() {
        return weatherProvider;
    }

    public Publisher getPublisher() {
        return publisher;
    }
}