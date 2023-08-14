package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import inet.dmsx.server.constants.Response;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public final class DeleteFileHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(DeleteFileHandler.class.getName());

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Delete file at " + params.filePath());

            Path fileToDeletePath = Paths.get(params.filePath());
            Files.deleteIfExists(fileToDeletePath);

            RoutingHandlers.sendOkMessage(exchange, Response.OK.getText());
            LOGGER.info("END Delete file at " + params.filePath());
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
