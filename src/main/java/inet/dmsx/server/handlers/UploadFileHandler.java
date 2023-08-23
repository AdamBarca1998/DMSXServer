package inet.dmsx.server.handlers;

import inet.dmsx.server.DMSXServer;
import inet.dmsx.server.Utils;
import inet.dmsx.server.state.IllegalStateServerException;
import io.undertow.server.HttpServerExchange;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class UploadFileHandler extends ManagementHandler {

    public UploadFileHandler(DMSXServer server) {
        super(server);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            if (blockExchange(exchange)) return;
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Upload file at " + params.filePath());
            checkState();

            var inputStream = exchange.getInputStream();

            Path targetPath = params.filePath();
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

            RoutingHandlers.okHandler(exchange, Response.OK.getText());
            LOGGER.info("END Upload file at " + params.filePath());
        } catch (IllegalStateServerException e) {
            RoutingHandlers.illegalStateServerHandler(exchange, e);
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}