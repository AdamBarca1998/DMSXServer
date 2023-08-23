package inet.dmsx.server.properties;

import inet.dmsx.server.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropertiesParserSingleton { //NOSONAR

    private static final PropertiesParserSingleton INSTANCE = new PropertiesParserSingleton();

    private final Properties appProps = new Properties();

    private PropertiesParserSingleton() {
        var path = Main.getConfigPath();

        try (var fis = new FileInputStream(path)){
            appProps.load(fis);
        } catch (IOException e) {
            throw new ErrorLoadConfigFileException("Can't load config file at: " + path);
        }
    }

    public static PropertiesParserSingleton getInstance() {
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
