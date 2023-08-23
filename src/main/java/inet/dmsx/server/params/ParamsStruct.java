package inet.dmsx.server.params;

import java.nio.file.Path;

public record ParamsStruct(
        String storageId,
        String directory,
        String fileName,
        Path filePath
) {
}
