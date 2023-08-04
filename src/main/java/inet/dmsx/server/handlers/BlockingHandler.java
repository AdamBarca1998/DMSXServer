package inet.dmsx.server.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public abstract class BlockingHandler implements HttpHandler {

    protected final Logger log = Logger.getLogger(UploadFileHandler.class.getName());

    protected boolean blockExchange(HttpServerExchange exchange) {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return true;
        }

        exchange.startBlocking();
        return false;
    }

    protected long getSizeInMb(String filePath) throws IOException {
        var sizeBytes = Files.size(Paths.get(filePath));

        return sizeBytes / 1024 / 1024;
    }
}
