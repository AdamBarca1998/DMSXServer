package inet.dmsx.server.handlers;

import inet.dmsx.server.DMSXServer;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.logging.Logger;

public final class ShutdownServerHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(ShutdownServerHandler.class.getName());

    private final DMSXServer server;

    public ShutdownServerHandler(DMSXServer server) {
        this.server = server;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        try {
            server.shutdown();
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
