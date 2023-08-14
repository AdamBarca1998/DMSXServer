package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

final public class ChecksumFileHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(ChecksumFileHandler.class.getName());

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Checksum file at " + params.filePath());

            try (InputStream is = Files.newInputStream(Paths.get(params.filePath()))) {
                String checksum = DigestUtils.md5Hex(is);

                RoutingHandlers.sendOkMessage(exchange, checksum);
            }

            LOGGER.info("END Checksum file at " + params.filePath());
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
