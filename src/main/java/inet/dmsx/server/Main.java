package inet.dmsx.server;

import org.quartz.SchedulerException;

public class Main {

    public static String CONFIG_PATH;

    public static void main(String[] args) throws SchedulerException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Missing configuration path argument!");
        }
        CONFIG_PATH = args[0];

        DMSXServer dmsxServer = new DMSXServer();

        dmsxServer.start();
    }
}