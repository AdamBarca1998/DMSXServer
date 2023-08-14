package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

public final class GetFileHandler extends BlockingHandler {

    private static final int STREAM_BUFFER_LENGTH = 4096;

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            if (blockExchange(exchange)) return;
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Get file at " + params.filePath());

            var file = new File(params.filePath());

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
            try (BufferedOutputStream targetStream = new BufferedOutputStream(exchange.getOutputStream(), STREAM_BUFFER_LENGTH);
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file), STREAM_BUFFER_LENGTH)
            ) {
                bufferedInputStream.transferTo(targetStream);
            }

            LOGGER.info("END Get file at " + params.filePath());
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}