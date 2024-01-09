package model;

public class Hotel {
    private String name;
    private String ratePerNight;
    private String totalRate;
    private String hotelClass;
    private String overallRating;
    private String islandName;
    private String islandCapitalCity;
    private String checkIn;
    private String checkOut;

    public Hotel(String name, String ratePerNight, String totalRate, String hotelClass, String overallRating,
                 String islandName, String islandCapitalCity, String checkIn, String checkOut) {
        this.name = name;
        this.ratePerNight = ratePerNight;
        this.totalRate = totalRate;
        this.hotelClass = hotelClass;
        this.overallRating = overallRating;
        this.islandName = islandName;
        this.islandCapitalCity = islandCapitalCity;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatePerNight() {
        return ratePerNight;
    }

    public void setRatePerNight(String ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }

    public String getHotelClass() {
        return hotelClass;
    }

    public void setHotelClass(String hotelClass) {
        this.hotelClass = hotelClass;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    public String getIslandName() {
        return islandName;
    }

    public void setIslandName(String islandName) {
        this.islandName = islandName;
    }

    public String getIslandCapitalCity() {
        return islandCapitalCity;
    }

    public void setIslandCapitalCity(String islandCapitalCity) {
        this.islandCapitalCity = islandCapitalCity;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", ratePerNight='" + ratePerNight + '\'' +
                ", totalRate='" + totalRate + '\'' +
                ", hotelClass='" + hotelClass + '\'' +
                ", overallRating='" + overallRating + '\'' +
                ", islandName='" + islandName + '\'' +
                ", islandCapitalCity='" + islandCapitalCity + '\'' +
                ", checkIn='" + checkIn + '\'' +
                ", checkOut='" + checkOut + '\'' +
                '}';
    }
}
