package inet.dmsx.server;

import inet.dmsx.server.constants.DMSXServerProperties;
import inet.dmsx.server.constants.PathParams;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

    public static ParamsStruct getParamsStruct(HttpServerExchange exchange) {
        var storageId = exchange.getQueryParameters().get(PathParams.STORAGE_ID.name()).getFirst();
        var directory = exchange.getQueryParameters().get(PathParams.DIRECTORY.name()).getFirst();
        var fileName = exchange.getQueryParameters().get(PathParams.FILE_NAME.name()).getFirst();
        var dirPath = PropertiesParserSingleton.getInstance().getPropertyValue(DMSXServerProperties.getDirPathOfStorageId(storageId));
        var filePath = dirPath + "\\" + directory + "\\" + fileName;

        return new ParamsStruct(storageId, directory, fileName, filePath);
    }

    public static long getSizeInMb(String filePath) throws IOException {
        var sizeBytes = Files.size(Paths.get(filePath));

        return sizeBytes / 1024 / 1024;
    }
}
