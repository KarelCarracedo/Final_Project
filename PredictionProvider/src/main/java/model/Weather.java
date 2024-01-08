package model;

public class Weather {
    private double temperature;
    private int humidity;
    private int clouds;
    private double windSpeed;
    private double precipitationProbability;
    private Location location;
    private String ts;
    private String ss;
    private String predictionTime;
    public Weather(double temperature, int humidity, int clouds, double windSpeed, double precipitationProbability, Location location,
                   String ts, String ss, String predictionTime) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.precipitationProbability = precipitationProbability;
        this.location = location;
        this.ts = ts;
        this.ss = ss;
        this.predictionTime = predictionTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getClouds() {
        return clouds;
    }

    public double getWindSpeed() { return windSpeed; }

    public double getPrecipitationProbability() {
        return precipitationProbability;
    }

    public Location getLocation() {
        return location;
    }

    public String getTs() { return ts; }

    public String getSs() { return ss; }

    public String getPredictionTime() { return predictionTime; }
}
