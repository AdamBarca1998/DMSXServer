package inet.dmsx.server.impl;

import inet.dmsx.server.state.PauseState;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.logging.Logger;

final class PauseServerHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(PauseServerHandler.class.getName());

    private final DMSXServer server;

    PauseServerHandler(DMSXServer server) {
        this.server = server;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            LOGGER.info("START Pause server");
            server.setState(new PauseState());
            RoutingHandlers.okHandler(exchange, Response.OK.getText());
            LOGGER.info("END Pause server");
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
