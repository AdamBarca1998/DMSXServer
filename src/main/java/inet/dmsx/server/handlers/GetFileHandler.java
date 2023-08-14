package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

public final class GetFileHandler extends BlockingHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            if (blockExchange(exchange)) return;
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Get file at " + params.filePath());

            var file = new File(params.filePath());

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
            try (BufferedOutputStream targetStream = new BufferedOutputStream(exchange.getOutputStream(), 4096);
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file), 4096)
            ) {
                bufferedInputStream.transferTo(targetStream);
            }

            LOGGER.info("END Get file at " + params.filePath());
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}