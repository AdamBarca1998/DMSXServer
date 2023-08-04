package inet.dmsx.server;

import inet.dmsx.server.enums.AppProperties;
import inet.dmsx.server.enums.PathParams;
import inet.dmsx.server.handlers.GetFileHandler;
import inet.dmsx.server.handlers.RoutingHandlers;
import inet.dmsx.server.handlers.UploadFileHandler;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public class DMSXServer {

    private static final PropertiesParserSingleton PROPERTIES_PARSER = PropertiesParserSingleton.getInstance();
    private static final int PORT = PROPERTIES_PARSER.getPropertyValueInt(AppProperties.PORT);
    private static final String HOST = PROPERTIES_PARSER.getPropertyValue(AppProperties.HOST);
    private static final String ROUTH_PATH = "/{" + PathParams.STORAGE_ID + "}/{" +
            PathParams.DIRECTORY + "}/{" + PathParams.FILE_NAME + "}";

    private final Undertow server;

    private static final HttpHandler ROUTES = new RoutingHandler()
            .get(ROUTH_PATH, new GetFileHandler())
            .post(ROUTH_PATH, new UploadFileHandler())
            .setFallbackHandler(RoutingHandlers::notFoundHandler);

    public DMSXServer() {
        server = Undertow.builder()
                .addHttpListener(PORT, HOST, ROUTES)
                .build();
    }

    public void start() {
        server.start();
    }
}
