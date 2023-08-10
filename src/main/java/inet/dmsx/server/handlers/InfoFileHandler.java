package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InfoFileHandler implements HttpHandler {

    private final Logger log = Logger.getLogger(InfoFileHandler.class.getName());

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            var params = Utils.getParamsStruct(exchange);

            log.log(Level.INFO, "START Info file at " + params.filePath());

            var size = Files.size(Paths.get(params.filePath())); // bytes

            RoutingHandlers.sendOkMessage(exchange, String.valueOf(size));
            log.log(Level.INFO, "END Info file at " + params.filePath());
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}