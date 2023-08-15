package inet.dmsx.server.handlers;

import inet.dmsx.server.DMSXServer;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.logging.Logger;

public abstract class ManagementHandler implements HttpHandler {

    protected static final Logger LOGGER = Logger.getLogger(ManagementHandler.class.getName());
    protected static final int STREAM_BUFFER_LENGTH = 4096;

    private final DMSXServer server;

    public ManagementHandler(DMSXServer server) {
        this.server = server;
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        server.getState().handleRequest();
    }

    protected boolean blockExchange(HttpServerExchange exchange) {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return true;
        }

        exchange.startBlocking();
        return false;
    }
}
