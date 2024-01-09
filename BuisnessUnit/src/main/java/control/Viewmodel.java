package control;

import model.Hotel;
import model.Weather;

import java.util.List;
import java.util.Scanner;

public class Viewmodel {
    private DbManager dbManager;

    public Viewmodel() {
        this.dbManager = new DbManager();
    }

    public void runCLI() {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\nOptions:");
            System.out.println("1. See hotel availability");
            System.out.println("2. Exit");

            System.out.print("Select an option: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    handleHotelAvailability(scanner);
                    break;
                case 2:
                    System.out.println("Exit.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid Option, try again.");
            }
        } while (option != 2);
    }

    private void handleHotelAvailability(Scanner scanner) {
        System.out.print("\nIntroduce the island: ");
        String targetIsland = scanner.nextLine();

        System.out.print("Introduce the capital city: ");
        String targetCapital = scanner.nextLine();

        List<Hotel> hotelList = dbManager.getHotelData(targetIsland, targetCapital);

        if (hotelList.isEmpty()) {
            System.out.println("\nNo hotels available: ");
            return;
        }

        System.out.println("\nHotels available:");

        for (Hotel hotel : hotelList) {
            System.out.println("------------------------------------------------");
            System.out.println("Name: " + hotel.getName());
            System.out.println("Classification: " + hotel.getHotelClass());
            System.out.println("Rate per night: " + hotel.getRatePerNight());
            System.out.println("Total rate: " + hotel.getTotalRate());
            System.out.println("Overall rating: " + hotel.getOverallRating());
            System.out.println("Check-in: " + hotel.getCheckIn());
            System.out.println("Check-out: " + hotel.getCheckOut());
            System.out.println("Island: " + hotel.getIslandName());
            System.out.println("Capital city: " + hotel.getIslandCapitalCity());
        }

        System.out.println("\nOptions:");
        System.out.println("1. See weather forecast");
        System.out.println("2. Check other availability");
        System.out.println("3. Exit");

        System.out.print("Select an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                handleWeatherPredictions(scanner);
                break;
            case 2:
                handleHotelAvailability(scanner);
                break;
            case 3:
                System.out.println("\nExit.");
                scanner.close();
                System.exit(0);
            default:
                System.out.println("\nInvalid option, try again.");
        }
    }

    private void handleWeatherPredictions(Scanner scanner) {
        System.out.print("\nIntroduce the capital city: ");
        String targetCapital = scanner.nextLine();

        List<Weather> weatherList = dbManager.getWeatherDataBetweenDates(targetCapital);

        if (weatherList.isEmpty()) {
            System.out.println("\nNo forecast available for selected zone.");
        } else {
            System.out.println("\nWeather forecast:");

            for (Weather weather : weatherList) {
                System.out.println("------------------------------------------------");
                System.out.println("Date: " + weather.getPredictionTime());
                System.out.println("Temperature: " + weather.getTemperature() + "°C");
                System.out.println("Wind speed: " + weather.getWindSpeed() + " m/s");
                System.out.println("Precipitation probability: " + weather.getPrecipitationProbability() + "%");
                System.out.println("Location: " + weather.getLocation());
            }
        }

        System.out.println("\nOptions:");
        System.out.println("1. Check other availability");
        System.out.println("2. Exit");

        System.out.print("Select an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                handleHotelAvailability(scanner);
                break;
            case 2:
                System.out.println("\nExit.");
                scanner.close();
                System.exit(0);
            default:
                System.out.println("\nInvalid option, try again.");
        }
    }
}
