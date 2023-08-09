package inet.dmsx.server.handlers;

import inet.dmsx.server.constants.Response;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class RoutingHandlers {

    public static void sendOkMessage(HttpServerExchange exchange, String msg) {
        exchange.setStatusCode(Response.OK.getCode());
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(msg);
    }

    public static void notFoundHandler(HttpServerExchange exchange) {
        exchange.setStatusCode(Response.PAGE_NOT_FOUND.getCode());
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(Response.PAGE_NOT_FOUND.getText());
    }
}
