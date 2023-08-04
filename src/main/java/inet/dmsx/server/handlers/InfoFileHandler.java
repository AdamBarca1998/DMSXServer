package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

public class InfoFileHandler extends BlockingHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws IOException {
        if (blockExchange(exchange)) return;

        var params = Utils.getParamsStruct(exchange);
        var size = Files.size(Paths.get(params.filePath()));

        log.log(Level.INFO, "Info file at " + params.filePath());
        RoutingHandlers.sendOkMessage(exchange, String.valueOf(size));
    }
}