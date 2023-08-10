package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InfoFileHandler implements HttpHandler {

    private final Logger log = Logger.getLogger(InfoFileHandler.class.getName());

    @Override
    public void handleRequest(HttpServerExchange exchange) throws IOException {
        var params = Utils.getParamsStruct(exchange);
        var size = Files.size(Paths.get(params.filePath())); // bytes

        log.log(Level.INFO, "Info file at " + params.filePath());
        RoutingHandlers.sendOkMessage(exchange, String.valueOf(size));
    }
}