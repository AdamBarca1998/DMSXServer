package inet.dmsx.server.handlers;

import inet.dmsx.server.DMSXServer;
import inet.dmsx.server.constants.Response;
import inet.dmsx.server.state.RunState;
import io.undertow.server.HttpServerExchange;

public final class ResumeServerHandler extends Handler {

    private final DMSXServer server;

    public ResumeServerHandler(DMSXServer server) {
        super(server);
        this.server = server;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            LOGGER.info("START Resume server");
            server.setState(new RunState());
            RoutingHandlers.sendOkMessage(exchange, Response.OK.getText());
            LOGGER.info("END Resume server");
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
