package inet.dmsx.server.impl;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.logging.Logger;

abstract class ManagementHandler implements HttpHandler {

    protected static final Logger LOGGER = Logger.getLogger(ManagementHandler.class.getName());
    protected static final int STREAM_BUFFER_LENGTH = 4096;

    private final DMSXServer server;

    protected ManagementHandler(DMSXServer server) {
        this.server = server;
    }

    protected void checkState() {
        server.getState().handle();
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
