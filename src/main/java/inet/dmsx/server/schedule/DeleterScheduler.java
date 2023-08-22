package inet.dmsx.server.schedule;

import inet.dmsx.server.DMSXServerProperties;
import inet.dmsx.server.PropertiesParserSingleton;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class DeleterScheduler {

    private static final PropertiesParserSingleton PROPERTIES_PARSER = PropertiesParserSingleton.getInstance();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final DeleterRunnable tmpRunnable;
    private final DeleterRunnable emailRunnable;

    public DeleterScheduler() {
        var tmpDeleteH = PROPERTIES_PARSER.getPropertyValueInt(DMSXServerProperties.TMP_DELETE_OLDER_HRS);
        var emailDeleteH = PROPERTIES_PARSER.getPropertyValueInt(DMSXServerProperties.EMAIL_DELETE_OLDER_HRS);

        tmpRunnable = new DeleterRunnable(
                PROPERTIES_PARSER.getPropertyValue(DMSXServerProperties.TMP_DIR_PATH),
                tmpDeleteH
        );
        emailRunnable = new DeleterRunnable(
                PROPERTIES_PARSER.getPropertyValue(DMSXServerProperties.EMAIL_DIR_PATH),
                emailDeleteH
        );
    }

    public void start() {
        scheduler.schedule(tmpRunnable, tmpRunnable.getHours(), TimeUnit.HOURS);
        scheduler.schedule(emailRunnable, emailRunnable.getHours(), TimeUnit.HOURS);
    }
}
