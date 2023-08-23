package inet.dmsx.server.properties;

import inet.dmsx.server.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropertiesParserSingleton {

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

    public String getPropertyValue(String s) {
        return appProps.getProperty(s, null);
    }

    public String getPropertyValue(DMSXServerProperties property) {
        return appProps.getProperty(property.getText(), null);
    }

    public int getPropertyValueInt(DMSXServerProperties property) {
        return Integer.parseInt(appProps.getProperty(property.getText(), null));
    }

    public int getPropertyValueInt(String s) {
        return Integer.parseInt(appProps.getProperty(s, null));
    }
}
