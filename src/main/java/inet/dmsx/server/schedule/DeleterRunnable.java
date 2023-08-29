package inet.dmsx.server.schedule;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

final class DeleterRunnable implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(DeleterRunnable.class.getName());

    private final String dir;
    private final int hours;

    DeleterRunnable(String dir, int hours) {
        this.dir = dir;
        this.hours = hours;
    }

    @Override
    public void run() {
        LOGGER.info(() -> "START Delete " + dir + " every " + hours + " hours.");

        try(var files = Files.walk(Path.of(dir), FileVisitOption.FOLLOW_LINKS)) {
            long cutoff = Instant.now().minus(Duration.ofMinutes(hours)).toEpochMilli();

            files.filter(p -> {
                try {
                    return p.toFile().isFile() && Files.getLastModifiedTime(p).toMillis() < cutoff;
                } catch (IOException e) {
                    throw new ErrorDeleteFileException("Something wrong with filtering files.");
                }
            }).forEach(file -> {
                try {
                    Files.deleteIfExists(file);
                    LOGGER.info(() -> "Delete file at: " + file);
                } catch (IOException e) {
                    throw new ErrorDeleteFileException("Can't delete file at: " + file);
                }
            });
        } catch (IOException e) {
            throw new ErrorDeleteFileException("Something wrong with walk in files.");
        }

        LOGGER.info(() -> "END Delete " + dir + " every " + hours + " hours.");
    }
}