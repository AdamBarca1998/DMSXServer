package inet.dmsx.server.handlers;

import inet.dmsx.server.DMSXServer;
import inet.dmsx.server.constants.Response;
import inet.dmsx.server.state.PauseState;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.logging.Logger;

public final class PauseServerHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(PauseServerHandler.class.getName());

    private final DMSXServer server;

    public PauseServerHandler(DMSXServer server) {
        this.server = server;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            LOGGER.info("START Pause server");
            server.setState(new PauseState());
            RoutingHandlers.sendOkMessage(exchange, Response.OK.getText());
            LOGGER.info("END Pause server");
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
