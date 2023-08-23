package inet.dmsx.server;

import inet.dmsx.server.params.ParamsStruct;
import inet.dmsx.server.params.PathParams;
import inet.dmsx.server.properties.DMSXServerProperties;
import inet.dmsx.server.properties.PropertiesParserSingleton;
import io.undertow.server.HttpServerExchange;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;

public final class Utils {

    private Utils() {
    }

    public static ParamsStruct getParamsStruct(HttpServerExchange exchange) {
        var storageId = exchange.getQueryParameters().get(PathParams.STORAGE_ID.name()).getFirst();
        var directory = exchange.getQueryParameters().get(PathParams.DIRECTORY.name()).getFirst();
        var fileName = exchange.getQueryParameters().get(PathParams.FILE_NAME.name()).getFirst();
        var dirPath = PropertiesParserSingleton.getInstance().getPropertyValue(storageId + DMSXServerProperties.DIR_PATH.getText());
        var filePath = Path.of(dirPath, directory, fileName);

        return new ParamsStruct(storageId, directory, fileName, filePath);
    }

    public static String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return sw.toString();
    }
}
