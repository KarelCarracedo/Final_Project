package model;

public class Weather {
    private double temperature;
    private double windSpeed;
    private double precipitationProbability;
    private String location;
    private String predictionTime;

    public Weather(double temperature, double windSpeed, double precipitationProbability, String location, String predictionTime) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.precipitationProbability = precipitationProbability;
        this.location = location;
        this.predictionTime = predictionTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getPrecipitationProbability() {
        return precipitationProbability;
    }

    public String getLocation() {
        return location;
    }

    public String getPredictionTime() {
        return predictionTime;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "temperature=" + temperature +
                ", windSpeed=" + windSpeed +
                ", precipitationProbability=" + precipitationProbability +
                ", location='" + location + '\'' +
                ", predictionTime='" + predictionTime + '\'' +
                '}';
    }
}