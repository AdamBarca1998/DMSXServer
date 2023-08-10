package inet.dmsx.server.handlers;

import inet.dmsx.server.constants.Response;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PingServerHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(PingServerHandler.class.getName());

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            LOGGER.info("START Ping server");
            RoutingHandlers.sendOkMessage(exchange, Response.OK.getText());
            LOGGER.info("END Ping server");
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}