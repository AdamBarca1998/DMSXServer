package inet.dmsx.server.handlers;

import inet.dmsx.server.DMSXServer;
import inet.dmsx.server.Utils;
import inet.dmsx.server.state.IllegalStateServerException;
import io.undertow.server.HttpServerExchange;

import java.nio.file.Files;
import java.nio.file.Path;

public final class DeleteFileHandler extends ManagementHandler {

    public DeleteFileHandler(DMSXServer server) {
        super(server);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Delete file at " + params.filePath());
            checkState();

            Path fileToDeletePath = Path.of(params.filePath());
            Files.deleteIfExists(fileToDeletePath);

            RoutingHandlers.okHandler(exchange, Response.OK.getText());
            LOGGER.info("END Delete file at " + params.filePath());
        } catch (IllegalStateServerException e) {
            RoutingHandlers.illegalStateServerHandler(exchange, e);
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
