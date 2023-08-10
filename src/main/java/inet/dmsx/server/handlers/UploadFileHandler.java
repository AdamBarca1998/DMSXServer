package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import inet.dmsx.server.constants.Response;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.logging.Level;

public class UploadFileHandler extends BlockingHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            if (blockExchange(exchange)) return;
            var params = Utils.getParamsStruct(exchange);

            log.log(Level.INFO, "START Upload file at " + params.filePath());

            var inputStream = exchange.getInputStream();

            File targetFile = new File(params.filePath());
            FileUtils.copyInputStreamToFile(inputStream, targetFile);

            RoutingHandlers.sendOkMessage(exchange, Response.OK.getText());
            log.log(Level.INFO, "END Upload file at " + params.filePath());
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}