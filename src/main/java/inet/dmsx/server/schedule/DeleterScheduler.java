package inet.dmsx.server.schedule;

import inet.dmsx.server.PropertiesParserSingleton;
import inet.dmsx.server.constants.DMSXServerProperties;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;

public final class DeleterScheduler {

    private static final PropertiesParserSingleton PROPERTIES_PARSER = PropertiesParserSingleton.getInstance();

    private final Scheduler scheduler = new StdSchedulerFactory().getScheduler();

    public DeleterScheduler() throws SchedulerException {
        var tmpHours = PROPERTIES_PARSER.getPropertyValueInt(DMSXServerProperties.TMP_DELETE_OLDER_HRS);
        var emailHours = PROPERTIES_PARSER.getPropertyValueInt(DMSXServerProperties.EMAIL_DELETE_OLDER_HRS);

        JobDetail tmpJob = newJob(DeleterJob.class)
                .withIdentity("tmpJob", "deleterJobGroup")
                .usingJobData("dir", PROPERTIES_PARSER.getPropertyValue(DMSXServerProperties.TMP_DIR_PATH))
                .usingJobData("hours", tmpHours)
                .build();
        JobDetail emailJob = newJob(DeleterJob.class)
                .withIdentity("emailJob", "deleterJobGroup")
                .usingJobData("dir", PROPERTIES_PARSER.getPropertyValue(DMSXServerProperties.EMAIL_DIR_PATH))
                .usingJobData("hours", emailHours)
                .build();
        Trigger tmpTrigger = TriggerBuilder.newTrigger()
                .withIdentity("tmpDeleter", "deleterTriggerGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(tmpHours)
                        .repeatForever())
                .build();
        Trigger emailTrigger = TriggerBuilder.newTrigger()
                .withIdentity("emailDeleter", "deleterTriggerGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(emailHours)
                        .repeatForever())
                .build();

        scheduler.scheduleJob(tmpJob, tmpTrigger);
        scheduler.scheduleJob(emailJob, emailTrigger);
    }

    public void start() throws SchedulerException {
        scheduler.start();
    }
}
