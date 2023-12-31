package inet.dmsx.server.impl;

import inet.dmsx.server.state.IllegalStateServerException;
import io.undertow.server.HttpServerExchange;

final class PingServerHandler extends ManagementHandler {

    PingServerHandler(DMSXServer server) {
        super(server);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            LOGGER.info("START Ping server");
            checkState();
            RoutingHandlers.okHandler(exchange, Response.OK.getText());
            LOGGER.info("END Ping server");
        } catch (IllegalStateServerException e) {
            RoutingHandlers.illegalStateServerHandler(exchange, e);
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}