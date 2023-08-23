package inet.dmsx.server.impl;

import inet.dmsx.server.DMSXServer;
import inet.dmsx.server.state.RunState;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.logging.Logger;

public final class ResumeServerHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(ResumeServerHandler.class.getName());

    private final DMSXServer server;

    public ResumeServerHandler(DMSXServer server) {
        this.server = server;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            LOGGER.info("START Resume server");
            server.setState(new RunState());
            RoutingHandlers.okHandler(exchange, Response.OK.getText());
            LOGGER.info("END Resume server");
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
