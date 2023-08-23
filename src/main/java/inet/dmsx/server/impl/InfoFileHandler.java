package inet.dmsx.server.impl;

import inet.dmsx.server.Utils;
import inet.dmsx.server.state.IllegalStateServerException;
import io.undertow.server.HttpServerExchange;

import java.nio.file.Files;

final class InfoFileHandler extends ManagementHandler {

    InfoFileHandler(DMSXServer server) {
        super(server);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Info file at " + params.filePath());
            checkState();

            var size = Files.size(params.filePath()); // bytes

            RoutingHandlers.okHandler(exchange, String.valueOf(size));
            LOGGER.info("END Info file at " + params.filePath());
        } catch (IllegalStateServerException e) {
            RoutingHandlers.illegalStateServerHandler(exchange, e);
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}