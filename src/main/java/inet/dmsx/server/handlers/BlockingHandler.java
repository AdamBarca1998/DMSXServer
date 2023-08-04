package inet.dmsx.server.handlers;

import inet.dmsx.server.ParamsStruct;
import inet.dmsx.server.PropertiesParserSingleton;
import inet.dmsx.server.enums.AppProperties;
import inet.dmsx.server.enums.PathParams;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public abstract class BlockingHandler implements HttpHandler {

    protected final String FILE_PATH = PropertiesParserSingleton.getInstance().getPropertyValue(AppProperties.FILE_PATH);
    protected final Logger log = Logger.getLogger(UploadFileHandler.class.getName());

    protected boolean blockExchange(HttpServerExchange exchange) {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return true;
        }

        exchange.startBlocking();
        return false;
    }

    protected ParamsStruct getParamsStruct(HttpServerExchange exchange) {
        var storageId = exchange.getQueryParameters().get(PathParams.STORAGE_ID.name()).getFirst();
        var directory = exchange.getQueryParameters().get(PathParams.DIRECTORY.name()).getFirst();
        var fileName = exchange.getQueryParameters().get(PathParams.FILE_NAME.name()).getFirst();
        var filePath = FILE_PATH + "\\" + storageId + "\\" + directory + "\\" + fileName;

        return new ParamsStruct(storageId, directory, fileName, filePath);
    }

    protected long getSizeInMb(String filePath) throws IOException {
        var sizeBytes = Files.size(Paths.get(filePath));

        return sizeBytes / 1024 / 1024;
    }
}
