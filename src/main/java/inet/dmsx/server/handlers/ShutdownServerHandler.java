package inet.dmsx.server.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.logging.Logger;

public final class ShutdownServerHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(ShutdownServerHandler.class.getName());

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {

    }
}
