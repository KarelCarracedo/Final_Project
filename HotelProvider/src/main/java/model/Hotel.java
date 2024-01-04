package model;

import java.time.Instant;

public class Hotel {
    private String name;
    private String ratePerNight;
    private String totalRate;
    private String hotelClass;
    private String overallRating;
    private Island island;
    private String checkIn;
    private String checkOut;
    private Instant ts = Instant.now();
    private String ss;
    public Hotel(String name, String ratePerNight, String totalRate, String hotelClass, String overallRating, Island island, String checkIn, String checkOut,String ss) {
        this.name = name;
        this.ratePerNight = ratePerNight;
        this.totalRate = totalRate;
        this.hotelClass = hotelClass;
        this.overallRating = overallRating;
        this.island = island;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.ss = ss;
    }

    public String getName() {
        return name;
    }

    public String getRatePerNight() {
        return ratePerNight;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public String getHotelClass() {
        return hotelClass;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public Island getIsland() { return island; }

    public String getCheckIn() { return checkIn; }

    public String getCheckOut() { return checkOut; }

    public Instant getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }
}
