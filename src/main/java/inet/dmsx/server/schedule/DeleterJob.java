package inet.dmsx.server.schedule;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

public final class DeleterJob implements Job {

    private static final Logger LOGGER = Logger.getLogger(DeleterJob.class.getName());

    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String dir = dataMap.getString("dir");
        int hours = dataMap.getInt("hours");

        LOGGER.info("START Delete " + dir + " every " + hours + " hours.");

        long cutoff = Instant.now().minus(Duration.ofHours(hours)).toEpochMilli();

        try {
            Files.newDirectoryStream(Path.of(dir), p -> (p.toFile().lastModified()) < cutoff)
                    .forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        LOGGER.info("END Delete " + dir + " every " + hours + " hours.");
    }
}