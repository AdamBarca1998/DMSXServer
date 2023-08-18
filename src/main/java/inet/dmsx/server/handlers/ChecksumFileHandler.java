package inet.dmsx.server.handlers;

import inet.dmsx.server.DMSXServer;
import inet.dmsx.server.Utils;
import inet.dmsx.server.state.IllegalStateServerException;
import io.undertow.server.HttpServerExchange;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.HexFormat;

public final class ChecksumFileHandler extends ManagementHandler {

    private static final String MD5 = "MD5";

    public ChecksumFileHandler(DMSXServer server) {
        super(server);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Checksum file at " + params.filePath());
            super.handleRequest(exchange);

            MessageDigest md = MessageDigest.getInstance(MD5);
            try (var is = new BufferedInputStream(Files.newInputStream(Paths.get(params.filePath())), STREAM_BUFFER_LENGTH);
                 DigestInputStream dis = new DigestInputStream(is, md))
            {
                dis.readAllBytes();
            }
            String hex = HexFormat.of().formatHex(md.digest());

            RoutingHandlers.okHandler(exchange, hex);
            LOGGER.info("END Checksum file at " + params.filePath());
        } catch (IllegalStateServerException e) {
            RoutingHandlers.illegalStateServerHandler(exchange, e);
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
