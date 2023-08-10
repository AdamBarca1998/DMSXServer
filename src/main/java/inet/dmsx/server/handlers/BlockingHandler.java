package inet.dmsx.server.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.logging.Logger;

public abstract class BlockingHandler implements HttpHandler {

    protected static final Logger LOGGER = Logger.getLogger(BlockingHandler.class.getName());

    protected boolean blockExchange(HttpServerExchange exchange) {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return true;
        }

        exchange.startBlocking();
        return false;
    }
}
