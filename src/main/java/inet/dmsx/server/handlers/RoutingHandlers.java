package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class RoutingHandlers {

    private static final Logger LOGGER = Logger.getLogger(RoutingHandlers.class.getName());

    private RoutingHandlers() {
    }

    public static void okHandler(HttpServerExchange exchange, String msg) {
        createHandler(exchange, msg, Response.OK.getCode());
    }

    public static void notFoundHandler(HttpServerExchange exchange) {
        createHandler(exchange, Response.PAGE_NOT_FOUND.getText(), Response.PAGE_NOT_FOUND.getCode());
    }

    public static void exceptionHandler(HttpServerExchange exchange, Throwable e) {
        String stackTrace = Utils.getStackTrace(e);

        LOGGER.log(Level.WARNING, stackTrace);

        createHandler(exchange, stackTrace, Response.ERROR.getCode());
    }

    public static void illegalStateServerHandler(HttpServerExchange exchange, Throwable e) {
        String stackTrace = Utils.getStackTrace(e);

        LOGGER.log(Level.WARNING, stackTrace);

        createHandler(exchange, stackTrace, Response.ILLEGAL_STATE_SERVER.getCode());
    }

    public static void createHandler(HttpServerExchange exchange, String msg, int code) {
        exchange.setStatusCode(code);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(msg);
    }
}
