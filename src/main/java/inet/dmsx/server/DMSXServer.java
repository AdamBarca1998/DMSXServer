package inet.dmsx.server;

import inet.dmsx.server.impl.ChecksumFileHandler;
import inet.dmsx.server.impl.DeleteFileHandler;
import inet.dmsx.server.impl.GetFileHandler;
import inet.dmsx.server.impl.HealthServerHandler;
import inet.dmsx.server.impl.InfoFileHandler;
import inet.dmsx.server.impl.PauseServerHandler;
import inet.dmsx.server.impl.PingServerHandler;
import inet.dmsx.server.impl.ResumeServerHandler;
import inet.dmsx.server.impl.RoutingHandlers;
import inet.dmsx.server.impl.ShutdownServerHandler;
import inet.dmsx.server.impl.UploadFileHandler;
import inet.dmsx.server.params.PathParams;
import inet.dmsx.server.properties.DMSXServerProperties;
import inet.dmsx.server.properties.PropertiesParserSingleton;
import inet.dmsx.server.schedule.DeleterScheduler;
import inet.dmsx.server.state.RunState;
import inet.dmsx.server.state.ServerState;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public final class DMSXServer {

    private static final PropertiesParserSingleton PROPERTIES_PARSER = PropertiesParserSingleton.getInstance();
    private static final String ROUTH_PATH = "/{" + PathParams.STORAGE_ID + "}/{" +
            PathParams.DIRECTORY + "}/{" + PathParams.FILE_NAME + "}";

    public static final int PORT = PROPERTIES_PARSER.getPropertyValueInt(DMSXServerProperties.PORT);
    public static final String HOST = PROPERTIES_PARSER.getPropertyValue(DMSXServerProperties.HOST);

    private final Undertow server;
    private final DeleterScheduler deleterScheduler = new DeleterScheduler();
    private volatile ServerState state = new RunState(); //NOSONAR

    public DMSXServer() {
        HttpHandler routes = new RoutingHandler()
                // management
                .put("/management/pause", new PauseServerHandler(this))
                .put("/management/resume", new ResumeServerHandler(this))
                .put("/management/shutdown", new ShutdownServerHandler())
                // files
                .get(ROUTH_PATH + "/checksum", new ChecksumFileHandler(this))
                .get(ROUTH_PATH + "/info", new InfoFileHandler(this))
                .get(ROUTH_PATH, new GetFileHandler(this))
                .post(ROUTH_PATH, new UploadFileHandler(this))
                .delete(ROUTH_PATH, new DeleteFileHandler(this))
                // server
                .get("/ping", new PingServerHandler(this))
                .get("/health", new HealthServerHandler(this))
                .setFallbackHandler(RoutingHandlers::notFoundHandler);

        server = Undertow.builder()
                .addHttpListener(PORT, HOST, routes)
                .build();
    }

    public void start() {
        server.start();
        deleterScheduler.start();
    }

    public void setState(ServerState state) {
        this.state = state;
    }

    public ServerState getState() {
        return state;
    }
}
