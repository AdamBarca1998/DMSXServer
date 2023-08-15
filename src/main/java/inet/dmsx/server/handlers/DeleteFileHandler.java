package inet.dmsx.server.handlers;

import inet.dmsx.server.DMSXServer;
import inet.dmsx.server.Utils;
import inet.dmsx.server.constants.Response;
import inet.dmsx.server.state.IllegalStateServerException;
import io.undertow.server.HttpServerExchange;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DeleteFileHandler extends Handler {

    public DeleteFileHandler(DMSXServer server) {
        super(server);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Delete file at " + params.filePath());
            managementRequest();

            Path fileToDeletePath = Paths.get(params.filePath());
            Files.deleteIfExists(fileToDeletePath);

            RoutingHandlers.sendOkMessage(exchange, Response.OK.getText());
            LOGGER.info("END Delete file at " + params.filePath());
        } catch (IllegalStateServerException e) {
            RoutingHandlers.illegalStateServerHandler(exchange, e);
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
