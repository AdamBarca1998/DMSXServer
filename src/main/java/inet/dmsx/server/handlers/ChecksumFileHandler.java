package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChecksumFileHandler implements HttpHandler {

    private final Logger log = Logger.getLogger(UploadFileHandler.class.getName());

    @Override
    public void handleRequest(HttpServerExchange exchange) throws IOException {
        var params = Utils.getParamsStruct(exchange);

        try (InputStream is = Files.newInputStream(Paths.get(params.filePath()))) {
            String checksum = DigestUtils.md5Hex(is);

            RoutingHandlers.sendOkMessage(exchange, checksum);
        }

        log.log(Level.INFO, "Checksum file at " + params.filePath());
    }
}
