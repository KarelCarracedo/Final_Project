package control;

import javax.jms.JMSException;

public class Main {
    public static void main(String[] args) throws JMSException {
        BusinessControl businessControl = new BusinessControl(args[0]);
        businessControl.saveTables();
        businessControl.startCli();
    }
}
