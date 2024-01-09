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
                    System.out.println("Saliendo...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }
        } while (option != 2);
    }

    private void handleHotelAvailability(Scanner scanner) {
        System.out.print("\nIntroduce la isla que quieres visitar: ");
        String targetIsland = scanner.nextLine();

        System.out.print("Introduce el municipio (capital) que quieres visitar: ");
        String targetCapital = scanner.nextLine();

        List<Hotel> hotelList = dbManager.getHotelData(targetIsland, targetCapital);

        if (hotelList.isEmpty()) {
            System.out.println("\nNo hay hoteles disponibles en la ubicación seleccionada.");
            return;
        }

        System.out.println("\nHoteles disponibles:");

        for (Hotel hotel : hotelList) {
            System.out.println("------------------------------------------------");
            System.out.println("Nombre: " + hotel.getName());
            System.out.println("Clasificación: " + hotel.getHotelClass());
            System.out.println("Tarifa por noche: " + hotel.getRatePerNight());
            System.out.println("Tarifa total: " + hotel.getTotalRate());
            System.out.println("Puntuación general: " + hotel.getOverallRating());
            System.out.println("Check-in: " + hotel.getCheckIn());
            System.out.println("Check-out: " + hotel.getCheckOut());
            System.out.println("Isla: " + hotel.getIslandName());
            System.out.println("Capital: " + hotel.getIslandCapitalCity());
        }

        System.out.println("\nOpciones:");
        System.out.println("1. Ver predicciones de tiempo");
        System.out.println("2. Comprobar otra disponibilidad");
        System.out.println("3. Salir");

        System.out.print("Seleccione una opción: ");
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
                System.out.println("\nSaliendo...");
                scanner.close();
                System.exit(0);
            default:
                System.out.println("\nOpción no válida. Inténtelo de nuevo.");
        }
    }

    private void handleWeatherPredictions(Scanner scanner) {
        System.out.print("\nIntroduce el municipio (capital) que quieres visitar: ");
        String targetCapital = scanner.nextLine();

        List<Weather> weatherList = dbManager.getWeatherDataBetweenDates(targetCapital);

        if (weatherList.isEmpty()) {
            System.out.println("\nNo hay predicciones de tiempo disponibles para la ubicación seleccionada.");
        } else {
            System.out.println("\nPredicciones de tiempo:");

            for (Weather weather : weatherList) {
                System.out.println("------------------------------------------------");
                System.out.println("Fecha: " + weather.getPredictionTime());
                System.out.println("Temperatura: " + weather.getTemperature() + "°C");
                System.out.println("Velocidad del viento: " + weather.getWindSpeed() + " m/s");
                System.out.println("Probabilidad de precipitación: " + weather.getPrecipitationProbability() + "%");
                System.out.println("Ubicación: " + weather.getLocation());
            }
        }

        System.out.println("\nOpciones:");
        System.out.println("1. Comprobar otra disponibilidad");
        System.out.println("2. Salir");

        System.out.print("Seleccione una opción: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                handleHotelAvailability(scanner);
                break;
            case 2:
                System.out.println("\nSaliendo...");
                scanner.close();
                System.exit(0);
            default:
                System.out.println("\nOpción no válida. Inténtelo de nuevo.");
        }
    }
}
