package control;

import model.Hotel;
import model.Island;

import java.util.List;

public interface HotelProvider {
    List<Hotel> getHotels(Island island);
}
