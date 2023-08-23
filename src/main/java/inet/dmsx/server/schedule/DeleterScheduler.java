package inet.dmsx.server.schedule;

import inet.dmsx.server.properties.DMSXServerProperties;
import inet.dmsx.server.properties.PropertiesParserSingleton;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class DeleterScheduler {

    private static final PropertiesParserSingleton PROPERTIES_PARSER = PropertiesParserSingleton.getInstance();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final DeleterRunnable tmpRunnable;
    private final DeleterRunnable emailRunnable;

    public DeleterScheduler() {
        var tmpDeleteH = PROPERTIES_PARSER.getPropertyValueInt("tmp" + DMSXServerProperties.DELETE_OLDER_HRS.getText());
        var emailDeleteH = PROPERTIES_PARSER.getPropertyValueInt("email" + DMSXServerProperties.DELETE_OLDER_HRS.getText());

        tmpRunnable = new DeleterRunnable(
                PROPERTIES_PARSER.getPropertyValue("tmp" + DMSXServerProperties.DIR_PATH.getText()),
                tmpDeleteH
        );
        emailRunnable = new DeleterRunnable(
                PROPERTIES_PARSER.getPropertyValue("email" + DMSXServerProperties.DIR_PATH.getText()),
                emailDeleteH
        );
    }

    public void start() {
        scheduler.schedule(tmpRunnable, tmpRunnable.getHours(), TimeUnit.HOURS);
        scheduler.schedule(emailRunnable, emailRunnable.getHours(), TimeUnit.HOURS);
    }
}
