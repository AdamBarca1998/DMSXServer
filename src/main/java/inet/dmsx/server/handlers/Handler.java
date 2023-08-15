package inet.dmsx.server.handlers;

import inet.dmsx.server.DMSXServer;
import inet.dmsx.server.state.IllegalStateServerException;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.logging.Logger;

public abstract class Handler implements HttpHandler {

    protected static final Logger LOGGER = Logger.getLogger(Handler.class.getName());
    protected static final int STREAM_BUFFER_LENGTH = 4096;

    private final DMSXServer server;

    public Handler(DMSXServer server) {
        this.server = server;
    }

    protected void managementRequest() throws IllegalStateServerException {
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
