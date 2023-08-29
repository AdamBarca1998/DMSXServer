package inet.dmsx.server.impl;

import inet.dmsx.server.params.PathParams;
import inet.dmsx.server.properties.DMSXServerProperties;
import inet.dmsx.server.properties.PropertiesParserSingleton;
import inet.dmsx.server.schedule.DeleterScheduler;
import inet.dmsx.server.state.RunState;
import inet.dmsx.server.state.ServerState;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

import static io.undertow.UndertowOptions.ALWAYS_SET_KEEP_ALIVE;
import static io.undertow.UndertowOptions.ENABLE_HTTP2;

public final class DMSXServer {

    private static final PropertiesParserSingleton PROPERTIES_PARSER = PropertiesParserSingleton.getInstance();
    private static final String ROUTH_PATH = "/{" + PathParams.STORAGE_ID + "}/{" +
            PathParams.DIRECTORY + "}/{" + PathParams.FILE_NAME + "}";

    public static final int PORT = PROPERTIES_PARSER.getPropertyValueInt(DMSXServerProperties.PORT);
    public static final String HOST = PROPERTIES_PARSER.getPropertyValue(DMSXServerProperties.HOST);

    private final Undertow server;
    private final DeleterScheduler deleterScheduler = new DeleterScheduler();
    private volatile ServerState state = new RunState();

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
                .setServerOption(ENABLE_HTTP2, true)
                .setServerOption(ALWAYS_SET_KEEP_ALIVE, true)
                .build();
    }

    public void start() {
        server.start();
        deleterScheduler.start();
    }

    public void stop() {
        server.stop();
        deleterScheduler.stop();
    }

    public void setState(ServerState state) {
        this.state = state;
    }

    public ServerState getState() {
        return state;
    }
}
