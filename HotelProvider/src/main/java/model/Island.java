package model;

public class Island {
    private String name;
    private String capitalCity;
    public Island(String name, String capitalCity) {
        this.name = name;
        this.capitalCity = capitalCity;
    }

    public String getName() {
        return name;
    }

    public String getCapitalCity() {
        return capitalCity;
    }
}
