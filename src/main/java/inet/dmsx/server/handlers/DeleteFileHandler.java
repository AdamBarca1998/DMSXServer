package inet.dmsx.server.handlers;

import inet.dmsx.server.Utils;
import inet.dmsx.server.constants.Response;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteFileHandler implements HttpHandler {

    private final Logger log = Logger.getLogger(DeleteFileHandler.class.getName());

    @Override
    public void handleRequest(HttpServerExchange exchange) throws IOException {
        var params = Utils.getParamsStruct(exchange);

        log.log(Level.INFO, "START Delete file at " + params.filePath());

        FileUtils.touch(new File(params.filePath()));
        File fileToDelete = FileUtils.getFile(params.filePath());
        FileUtils.deleteQuietly(fileToDelete);

        RoutingHandlers.sendOkMessage(exchange, Response.OK.getText());
        log.log(Level.INFO, "END Delete file at " + params.filePath());
    }
}
