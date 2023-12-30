package model;

import java.time.Instant;

public class Hotel {
    private String name;
    private int ratePerNight;
    private int totalRate;
    private int hotelClass;
    private double overallRating;
    private Island island;
    private String checkIn;
    private String checkOut;
    private Instant ts = Instant.now();
    private String ss;
    public Hotel(String name, int ratePerNight, int totalRate, int hotelClass, double overallRating, Island island, String checkIn, String checkOut,String ss) {
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

    public int getRatePerNight() {
        return ratePerNight;
    }

    public int getTotalRate() {
        return totalRate;
    }

    public int getHotelClass() {
        return hotelClass;
    }

    public double getOverallRating() {
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
