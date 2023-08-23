package inet.dmsx.server.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import inet.dmsx.server.Utils;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class RoutingHandlers {

    private static final Logger LOGGER = Logger.getLogger(RoutingHandlers.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private RoutingHandlers() {
    }

    public static void okHandler(HttpServerExchange exchange, String msg) {
        putHandlerToExchange(exchange, msg, Response.OK.getCode());
    }

    public static void okJsonHandler(HttpServerExchange exchange, Object obj) throws JsonProcessingException {
        exchange.setStatusCode(Response.OK.getCode());
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(mapper.writeValueAsString(obj));
    }

    public static void notFoundHandler(HttpServerExchange exchange) {
        putHandlerToExchange(exchange, Response.PAGE_NOT_FOUND.getText(), Response.PAGE_NOT_FOUND.getCode());
    }

    public static void exceptionHandler(HttpServerExchange exchange, Throwable e) {
        String stackTrace = Utils.getStackTrace(e);

        LOGGER.log(Level.WARNING, stackTrace);

        putHandlerToExchange(exchange, stackTrace, Response.ERROR.getCode());
    }

    public static void illegalStateServerHandler(HttpServerExchange exchange, Throwable e) {
        String stackTrace = Utils.getStackTrace(e);

        LOGGER.log(Level.WARNING, stackTrace);

        putHandlerToExchange(exchange, stackTrace, Response.ILLEGAL_STATE_SERVER.getCode());
    }

    public static void putHandlerToExchange(HttpServerExchange exchange, String msg, int code) {
        exchange.setStatusCode(code);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(msg);
    }
}
