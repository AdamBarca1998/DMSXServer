package inet.dmsx.server.impl;

import inet.dmsx.server.Utils;
import inet.dmsx.server.state.IllegalStateServerException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

final class GetFileHandler extends ManagementHandler {

    GetFileHandler(DMSXServer server) {
        super(server);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            if (blockExchange(exchange)) return;
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Get file at " + params.filePath());
            checkState();

            var file = params.filePath().toFile();

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file), STREAM_BUFFER_LENGTH)) {
                bufferedInputStream.transferTo(exchange.getOutputStream());
            }

            LOGGER.info("END Get file at " + params.filePath());
        } catch (IllegalStateServerException e) {
            RoutingHandlers.illegalStateServerHandler(exchange, e);
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}