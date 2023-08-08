package inet.dmsx.server.handlers;

import inet.dmsx.server.enums.ResponseString;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class RoutingHandlers {

    public static void sendOkMessage(HttpServerExchange exchange, String msg) {
        exchange.setStatusCode(StatusCodes.OK);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(msg);
    }

    public static void notFoundHandler(HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.NOT_FOUND);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(ResponseString.Page_Not_Found.getText());
    }
}
