package control;

import model.Location;
import model.Weather;

import java.util.List;

public interface WeatherProvider {
    List<Weather> get(Location location);
}
