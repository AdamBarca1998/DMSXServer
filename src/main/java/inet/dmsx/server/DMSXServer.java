package inet.dmsx.server;

import inet.dmsx.server.constants.DMSXServerProperties;
import inet.dmsx.server.constants.PathParams;
import inet.dmsx.server.handlers.ChecksumFileHandler;
import inet.dmsx.server.handlers.DeleteFileHandler;
import inet.dmsx.server.handlers.GetFileHandler;
import inet.dmsx.server.handlers.InfoFileHandler;
import inet.dmsx.server.handlers.PingServerHandler;
import inet.dmsx.server.handlers.RoutingHandlers;
import inet.dmsx.server.handlers.UploadFileHandler;
import inet.dmsx.server.schedule.DeleterScheduler;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import org.quartz.SchedulerException;

public class DMSXServer {

    private static final PropertiesParserSingleton PROPERTIES_PARSER = PropertiesParserSingleton.getInstance();
    private static final String ROUTH_PATH = "/{" + PathParams.STORAGE_ID + "}/{" +
            PathParams.DIRECTORY + "}/{" + PathParams.FILE_NAME + "}";
    private static final HttpHandler ROUTES = new RoutingHandler()
            .get("/ping", new PingServerHandler())
            .get(ROUTH_PATH + "/checksum", new ChecksumFileHandler())
            .get(ROUTH_PATH + "/info", new InfoFileHandler())
            .get(ROUTH_PATH, new GetFileHandler())
            .post(ROUTH_PATH, new UploadFileHandler())
            .delete(ROUTH_PATH, new DeleteFileHandler())
            .setFallbackHandler(RoutingHandlers::notFoundHandler);
    private static final HttpHandler ROOT = Handlers.exceptionHandler(ROUTES)
            .addExceptionHandler(Throwable.class, RoutingHandlers::exceptionHandler);

    public static final int PORT = PROPERTIES_PARSER.getPropertyValueInt(DMSXServerProperties.PORT);
    public static final String HOST = PROPERTIES_PARSER.getPropertyValue(DMSXServerProperties.HOST);

    private final Undertow server;
    private final DeleterScheduler deleterScheduler = new DeleterScheduler();

    public DMSXServer() throws SchedulerException {
        server = Undertow.builder()
                .addHttpListener(PORT, HOST, ROOT)
                .build();
    }

    public void start() throws SchedulerException {
        server.start();
        deleterScheduler.start();
    }
}
