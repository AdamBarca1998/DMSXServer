package inet.dmsx.server.handlers;

import inet.dmsx.server.enums.ResponseString;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class RoutingHandlers {

    public static void allIsOk(HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.OK);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(ResponseString.OK.getText());
    }

    public static void somethingWrong(HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.BAD_REQUEST);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(ResponseString.SOMETHING_WRONG.getText());
    }

    public static void notFoundHandler(HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.NOT_FOUND);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(ResponseString.Page_Not_Found.getText());
    }
}
