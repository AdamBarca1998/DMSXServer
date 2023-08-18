package inet.dmsx.server.handlers;

import inet.dmsx.server.DMSXServer;
import inet.dmsx.server.Utils;
import inet.dmsx.server.state.IllegalStateServerException;
import io.undertow.server.HttpServerExchange;

import java.nio.file.Files;
import java.nio.file.Path;

public final class InfoFileHandler extends ManagementHandler {

    public InfoFileHandler(DMSXServer server) {
        super(server);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Info file at " + params.filePath());
            super.handleRequest(exchange);

            var size = Files.size(Path.of(params.filePath())); // bytes

            RoutingHandlers.okHandler(exchange, String.valueOf(size));
            LOGGER.info("END Info file at " + params.filePath());
        } catch (IllegalStateServerException e) {
            RoutingHandlers.illegalStateServerHandler(exchange, e);
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}