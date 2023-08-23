package inet.dmsx.server;

import inet.dmsx.server.properties.DMSXServerProperties;
import inet.dmsx.server.properties.PropertiesParserSingleton;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

public class Main {

    public static String CONFIG_PATH;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Missing configuration path argument!");
        }
        CONFIG_PATH = args[0];
        var propertiesParser = PropertiesParserSingleton.getInstance();
        LogManager.getLogManager().readConfiguration(new FileInputStream(propertiesParser.getPropertyValue("log" + DMSXServerProperties.FILE_PATH.getText())));

        DMSXServer dmsxServer = new DMSXServer();

        dmsxServer.start();
    }
}