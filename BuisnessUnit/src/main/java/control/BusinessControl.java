package control;

import javax.jms.JMSException;

public class BusinessControl {
    private HotelConsumer hotelConsumer;
    private WeatherConsumer weatherConsumer;
    private DatalakeCollector datalakeCollector;
    private Viewmodel viewmodel;
    public BusinessControl(String datalakePath) {
        this.hotelConsumer = new HotelConsumer();
        this.weatherConsumer = new WeatherConsumer();
        this.datalakeCollector = new DatalakeCollector(datalakePath);
        this.viewmodel = new Viewmodel();
    }

    public void saveTables() throws JMSException {
        datalakeCollector.collect();
        hotelConsumer.hotelConsumeBroker();
        weatherConsumer.weatherConsumeBroker();
    }

    public void startCli() {
        viewmodel.runCLI();
    }
}
