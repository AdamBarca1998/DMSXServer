package inet.dmsx.server;

import inet.dmsx.server.constants.DMSXServerProperties;
import inet.dmsx.server.constants.PathParams;
import inet.dmsx.server.handlers.ChecksumFileHandler;
import inet.dmsx.server.handlers.DeleteFileHandler;
import inet.dmsx.server.handlers.GetFileHandler;
import inet.dmsx.server.handlers.InfoFileHandler;
import inet.dmsx.server.handlers.PauseServerHandler;
import inet.dmsx.server.handlers.PingServerHandler;
import inet.dmsx.server.handlers.ResumeServerHandler;
import inet.dmsx.server.handlers.RoutingHandlers;
import inet.dmsx.server.handlers.ShutdownServerHandler;
import inet.dmsx.server.handlers.UploadFileHandler;
import inet.dmsx.server.schedule.DeleterScheduler;
import inet.dmsx.server.state.RunState;
import inet.dmsx.server.state.ServerState;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.quartz.SchedulerException;

import java.time.LocalDateTime;

public final class DMSXServer {

    private static final PropertiesParserSingleton PROPERTIES_PARSER = PropertiesParserSingleton.getInstance();
    private static final String ROUTH_PATH = "/{" + PathParams.STORAGE_ID + "}/{" +
            PathParams.DIRECTORY + "}/{" + PathParams.FILE_NAME + "}";

    public static final int PORT = PROPERTIES_PARSER.getPropertyValueInt(DMSXServerProperties.PORT);
    public static final String HOST = PROPERTIES_PARSER.getPropertyValue(DMSXServerProperties.HOST);

    private final Undertow server;
    private final DeleterScheduler deleterScheduler = new DeleterScheduler();
    private final GracefulShutdownHandler shutdownHandler = new GracefulShutdownHandler(new ShutdownServerHandler(this));
    private ServerState state = new RunState();
    public DMSXServer() throws SchedulerException {
        HttpHandler routes = new RoutingHandler()
                // management
                .put("/management/pause", new PauseServerHandler(this))
                .put("/management/resume", new ResumeServerHandler(this))
                .put("/management/shutdown", shutdownHandler)
                // files
                .get(ROUTH_PATH + "/checksum", new ChecksumFileHandler(this))
                .get(ROUTH_PATH + "/info", new InfoFileHandler(this))
                .get(ROUTH_PATH, new GetFileHandler(this))
                .post(ROUTH_PATH, new UploadFileHandler(this))
                .delete(ROUTH_PATH, new DeleteFileHandler(this))
                // server
                .get("/ping", new PingServerHandler(this))
                .setFallbackHandler(RoutingHandlers::notFoundHandler);

        server = Undertow.builder()
                .addHttpListener(PORT, HOST, routes)
                .build();
    }

    public void start() throws SchedulerException {
        server.start();
        deleterScheduler.start();
    }

    public void shutdown() {
        shutdownHandler.shutdown();
        shutdownHandler.addShutdownListener(this::showdownListener);
    }

    public void setState(ServerState state) {
        this.state = state;
    }

    public ServerState getState() {
        return state;
    }

    private void showdownListener(boolean b) {
        System.out.println("sadasd " + LocalDateTime.now());
    }
}
