package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.logging.Logger;

public final class ChecksumFileHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(ChecksumFileHandler.class.getName());
    private static final int STREAM_BUFFER_LENGTH = 4096;
    private static final String MD5 = "MD5";

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Checksum file at " + params.filePath());

            MessageDigest md = MessageDigest.getInstance(MD5);
            try (var is = new BufferedInputStream(Files.newInputStream(Paths.get(params.filePath())), STREAM_BUFFER_LENGTH);
                 DigestInputStream dis = new DigestInputStream(is, md))
            {
                while (dis.read() != -1) {}
            }
            String hex = HexFormat.of().formatHex(md.digest());

            RoutingHandlers.sendOkMessage(exchange, hex);
            LOGGER.info("END Checksum file at " + params.filePath());
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
