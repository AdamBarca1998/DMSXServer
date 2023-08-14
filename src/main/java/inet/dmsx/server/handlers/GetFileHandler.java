package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

final public class GetFileHandler extends BlockingHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            if (blockExchange(exchange)) return;
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Get file at " + params.filePath());

            var file = new File(params.filePath());

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
            try (InputStream inputStream = new FileInputStream(file);
                 OutputStream targetStream = exchange.getOutputStream()
            ) {
                if (Utils.getSizeInMb(params.filePath()) < 2000) {
                    IOUtils.copy(inputStream, targetStream);
                } else {
                    IOUtils.copyLarge(inputStream, targetStream);
                }
            }

            LOGGER.info("END Get file at " + params.filePath());
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}