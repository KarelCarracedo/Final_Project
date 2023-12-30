package control;

public class Main {
    public static void main(String[] args) {
        WeatherControl weatherControl = new WeatherControl(args[0]);
        weatherControl.fetchAndSaveWeather();
    }
}