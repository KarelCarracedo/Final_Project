package control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Island;
import model.Hotel;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

public class SerpHotel implements HotelProvider{
    private final String apiKey;
    private final String ss = "SerpApi";
    public SerpHotel(String apiKey) { this.apiKey = apiKey; }
    @Override
    public List<Hotel> getHotels(Island island) {
        Date tomorrow = getTomorrowDate();
        Date fiveDaysAhead = getFutureDate(tomorrow, 5);
        String checkIn = formatDate(tomorrow);
        String checkOut = formatDate(fiveDaysAhead);
        String baseUrl = "https://serpapi.com/search.json?engine=google_hotels&q=" +
                island.getCapitalCity() + "&gl=es&hl=es&currency=EUR&check_in_date=" +
                checkIn + "&check_out_date=" + checkOut + "&api_key=" + apiKey;
        List<Hotel> hotelList = new ArrayList<>();

        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(baseUrl);
            HttpResponse response = httpClient.execute(request);

            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(reader, JsonObject.class);

            JsonArray hotels = jsonResponse.getAsJsonArray("properties");
            for (JsonElement hotelElement : hotels) {
                JsonObject hotelInfo = hotelElement.getAsJsonObject();
                String name = hotelInfo.get("name").getAsString();
                String ratePerNight = "NULL";
                if (hotelInfo.getAsJsonObject().has("rate_per_night")) {
                    ratePerNight = hotelInfo.getAsJsonObject("rate_per_night").get("extracted_lowest").getAsString();
                }
                String totalRate = "NULL";
                if (hotelInfo.getAsJsonObject().has("total_rate")) {
                    totalRate = hotelInfo.getAsJsonObject("total_rate").get("extracted_lowest").getAsString();
                }
                String hotelClass = "NULL";
                if (hotelInfo.getAsJsonObject().has("extracted_hotel_class")) {
                    hotelClass = hotelInfo.get("extracted_hotel_class").getAsString();
                }
                String overallRating = "NULL";
                if (hotelInfo.getAsJsonObject().has("overall_rating")) {
                    overallRating = hotelInfo.get("overall_rating").getAsString();
                }
                Hotel hotel = new Hotel(name, ratePerNight, totalRate, hotelClass, overallRating, island, checkIn, checkOut,getSs());
                hotelList.add(hotel);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return hotelList;
    }
    private static Date getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    private static Date getFutureDate(Date startDate, int daysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return calendar.getTime();
    }

    private static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
    public String getSs() {
        return ss;
    }
}
