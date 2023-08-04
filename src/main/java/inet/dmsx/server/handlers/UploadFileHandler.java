package inet.dmsx.server.handlers;

import io.undertow.server.HttpServerExchange;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class UploadFileHandler extends BlockingHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws IOException {
        if (blockExchange(exchange)) return;

        var params = getParamsStruct(exchange);
        var inputStream = exchange.getInputStream();

        File targetFile = new File(params.filePath());
        FileUtils.copyInputStreamToFile(inputStream, targetFile);

        log.log(Level.INFO, "Create file at " + params.filePath());
        RoutingHandlers.allIsOk(exchange);
    }
}