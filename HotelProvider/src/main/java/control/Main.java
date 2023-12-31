package control;

public class Main {
    public static void main(String[] args) {
        HotelControl hotelControl = new HotelControl(args[0]);
        hotelControl.fetchAndSaveHotel();
    }
}
