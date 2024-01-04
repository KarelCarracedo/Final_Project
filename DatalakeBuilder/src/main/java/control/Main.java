package control;

import javax.jms.JMSException;

public class Main {
    public static void main(String[] args) throws JMSException {
        System.out.println(args[0]);
        WeatherReceiver receiver = new WeatherReceiver(args[0]);
        HotelReceiver hotelReceiver = new HotelReceiver(args[0]);
        receiver.gsonReceiver();
        hotelReceiver.hotelInfoReceiver();
    }
}
