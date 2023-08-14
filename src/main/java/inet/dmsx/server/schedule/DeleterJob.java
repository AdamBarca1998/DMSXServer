package inet.dmsx.server.schedule;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.io.filefilter.TrueFileFilter.TRUE;

final public class DeleterJob implements Job {

    private static final Logger LOGGER = Logger.getLogger(DeleterJob.class.getName());

    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String dir = dataMap.getString("dir");
        int hours = dataMap.getInt("hours");

        LOGGER.info("START Delete " + dir + " every " + hours + " hours.");

        Instant cutoff = Instant.now().minus(Duration.ofHours(hours));
        Iterator<File> filesToDelete = FileUtils.iterateFiles(new File(dir), new AgeFileFilter(cutoff), TRUE);

        filesToDelete.forEachRemaining(f -> {
            if (f.delete()) {
                LOGGER.info("Delete file: " + f.getName());
            } else {
                LOGGER.log(Level.WARNING, "Can't delete file: " + f.getName());
            }
        });

        LOGGER.info("END Delete " + dir + " every " + hours + " hours.");
    }
}