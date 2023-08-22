package inet.dmsx.server.schedule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

final class DeleterRunnable implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(DeleterRunnable.class.getName());

    private final String dir;
    private final int hours;

    public DeleterRunnable(String dir, int hours) {
        this.dir = dir;
        this.hours = hours;
    }

    @Override
    public void run() {
        LOGGER.info(() -> "START Delete " + dir + " every " + hours + " hours.");

        try {
            recursiveDeleteFilesOlderThanNHours(dir, hours);
        } catch (IOException e) {
            throw new ErrorDeleteFileException("Something wrong with delete directory at: " + dir);
        }

        LOGGER.info(() -> "END Delete " + dir + " every " + hours + " hours.");
    }

    private void recursiveDeleteFilesOlderThanNHours(String dirPath, int hours) throws IOException {
        long cutoff = Instant.now().minus(Duration.ofHours(hours)).toEpochMilli();

        var files = Files.list(Path.of(dirPath));

        files.forEach(path -> {
            if (Files.isDirectory(path)) {
                try {
                    recursiveDeleteFilesOlderThanNHours(path.toString(), hours);
                } catch (IOException e) {
                    throw new ErrorDeleteFileException("Something wrong with delete directory at: " + path);
                }
            } else {
                try {
                    if (Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS) < cutoff) {
                        Files.deleteIfExists(path);
                    }
                } catch (IOException e) {
                    throw new ErrorDeleteFileException("Can't delete file at: " + path);
                }
            }
        });

        files.close();
    }

    public int getHours() {
        return hours;
    }
}