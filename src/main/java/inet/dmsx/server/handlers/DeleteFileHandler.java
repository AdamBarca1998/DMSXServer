package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import inet.dmsx.server.constants.Response;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.logging.Logger;

final public class DeleteFileHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(DeleteFileHandler.class.getName());

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            var params = Utils.getParamsStruct(exchange);

            LOGGER.info("START Delete file at " + params.filePath());

            FileUtils.touch(new File(params.filePath()));
            File fileToDelete = FileUtils.getFile(params.filePath());
            FileUtils.deleteQuietly(fileToDelete);

            RoutingHandlers.sendOkMessage(exchange, Response.OK.getText());
            LOGGER.info("END Delete file at " + params.filePath());
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }
}
