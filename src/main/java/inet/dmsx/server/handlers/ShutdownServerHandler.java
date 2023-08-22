package inet.dmsx.server.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.logging.Logger;

public final class ShutdownServerHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(ShutdownServerHandler.class.getName());

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            LOGGER.info("Shutdown server");
            RoutingHandlers.okHandler(exchange, Response.OK.getText());
            System.exit(0);
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
