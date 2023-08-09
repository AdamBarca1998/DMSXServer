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
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

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

    public static final int PORT = PROPERTIES_PARSER.getPropertyValueInt(DMSXServerProperties.PORT);
    public static final String HOST = PROPERTIES_PARSER.getPropertyValue(DMSXServerProperties.HOST);

    private final Undertow server;

    public DMSXServer() {
        server = Undertow.builder()
                .addHttpListener(PORT, HOST, ROUTES)
                .build();
    }

    public void start() {
        server.start();
    }
}
