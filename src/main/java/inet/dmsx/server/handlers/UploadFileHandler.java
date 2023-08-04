package inet.dmsx.server.handlers;

import inet.dmsx.server.PropertiesParserSingleton;
import inet.dmsx.server.enums.AppProperties;
import inet.dmsx.server.enums.PathParams;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UploadFileHandler implements HttpHandler {

    private final String FILE_PATH = PropertiesParserSingleton.getInstance().getPropertyValue(AppProperties.FILE_PATH);
    private static final Logger log = Logger.getLogger(UploadFileHandler.class.getName());


    @Override
    public void handleRequest(HttpServerExchange exchange) throws IOException {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        exchange.startBlocking();

        var storageId = exchange.getQueryParameters().get(PathParams.STORAGE_ID.name()).getFirst();
        var directory = exchange.getQueryParameters().get(PathParams.DIRECTORY.name()).getFirst();
        var fileName = exchange.getQueryParameters().get(PathParams.FILE_NAME.name()).getFirst();
        var filePath = FILE_PATH + "\\" + storageId + "\\" + directory + "\\" + fileName;
        var inputStream = exchange.getInputStream();

        File targetFile = new File(filePath);
        FileUtils.copyInputStreamToFile(inputStream, targetFile);

        log.log(Level.INFO, "Create file at " + filePath);
        RoutingHandlers.allIsOk(exchange);
    }
}