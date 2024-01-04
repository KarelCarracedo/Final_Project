package control;

import model.Hotel;
import model.Island;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HotelControl {
    private HotelProvider hotelProvider;
    private HotelPublisher hotelPublisher;
    private String apiKey;
    public HotelControl(String apiKey) {
        this.hotelProvider = new SerpHotel(apiKey);
        this.hotelPublisher = new HotelPublisher();
    }

    public void fetchAndSaveHotel() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable hotelTask = () -> {
            List<Island> islands = readIslandsFromCSV("Islas.csv");
            for (Island island : islands) {
                List<Hotel> hotels = getHotelProvider().getHotels(island);
                for (Hotel hotel : hotels) {
                    try {
                        hotelPublisher.hotelPublish(hotel);
                    } catch (MyException e) {
                        System.out.println("Error");
                    }
                }
            }
        };
        scheduler.scheduleAtFixedRate(hotelTask, 0, 72, TimeUnit.HOURS);
    }

    private static List<Island> readIslandsFromCSV(String csvFile) {
        List<Island> islands = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] campos = line.split(",");

                String name = campos[0];
                String capitalCity = campos[1];

                Island island = new Island(name, capitalCity);
                islands.add(island);
            }

            for (Island island : islands) {
                System.out.println("Nombre: " + island.getName() +
                        ", Capital: " + island.getCapitalCity());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return islands;
    }

    public HotelProvider getHotelProvider() {
        return hotelProvider;
    }

    public HotelPublisher getPublisher() {
        return hotelPublisher;
    }
}
