package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import inet.dmsx.server.constants.Response;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class UploadFileHandler extends BlockingHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws IOException {
        if (blockExchange(exchange)) return;

        var params = Utils.getParamsStruct(exchange);
        var inputStream = exchange.getInputStream();

        File targetFile = new File(params.filePath());
        FileUtils.copyInputStreamToFile(inputStream, targetFile);

        log.log(Level.INFO, "Upload file at " + params.filePath());
        RoutingHandlers.sendOkMessage(exchange, Response.OK.getText());
    }
}