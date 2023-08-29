package inet.dmsx.server.schedule;

import inet.dmsx.server.properties.DMSXServerProperties;
import inet.dmsx.server.properties.PropertiesParserSingleton;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class DeleterScheduler {

    private static final Logger LOGGER = Logger.getLogger(DeleterScheduler.class.getName());
    private static final PropertiesParserSingleton PROPERTIES_PARSER = PropertiesParserSingleton.getInstance();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void start() {
        PROPERTIES_PARSER.getStorageIds().forEach(storageId -> {
            try {
                var deleteH = PROPERTIES_PARSER.getPropertyValueInt(storageId + DMSXServerProperties.DELETE_OLDER_HRS.getText());
                var runnable = new DeleterRunnable(
                        PROPERTIES_PARSER.getPropertyValue(storageId + DMSXServerProperties.DIR_PATH.getText()),
                        deleteH
                );

                scheduler.schedule(runnable, deleteH, TimeUnit.HOURS);

                LOGGER.info(() -> "Create runnable " + storageId + " with " + deleteH + " hours");
            } catch (NumberFormatException e) {
                LOGGER.info(() -> "Don't create runnable " + storageId);
            }
        });
    }

    public void stop() {
        scheduler.shutdown();
    }
}
