package inet.dmsx.server;

import inet.dmsx.server.enums.DMSXServerProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesParserSingleton {

    private static PropertiesParserSingleton INSTANCE;

    private final Properties appProps = new Properties();

    private PropertiesParserSingleton() {
        try {
            appProps.load(new FileInputStream(Main.CONFIG_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PropertiesParserSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PropertiesParserSingleton();
        }

        return INSTANCE;
    }

    public String getPropertyValue(DMSXServerProperties property) {
        return appProps.getProperty(property.getText(), "");
    }

    public int getPropertyValueInt(DMSXServerProperties property) {
        return Integer.parseInt(appProps.getProperty(property.getText(), "0"));
    }
}
