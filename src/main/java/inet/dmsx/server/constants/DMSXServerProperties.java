package inet.dmsx.server.constants;

import java.util.HashMap;
import java.util.Map;

public enum DMSXServerProperties {

    HOST("host"),
    PORT("port"),
    MNT_DIR_PATH("mnt.dir_path"),
    TMP_DIR_PATH("tmp.dir_path"),
    EMAIL_DIR_PATH("email.dir_path")
    ;

    DMSXServerProperties(String text) {
        this.text = text;
    }

    private static final Map<String, DMSXServerProperties> BY_TEXT = new HashMap<>();

    static {
        for (DMSXServerProperties e: values()) {
            BY_TEXT.put(e.getText(), e);
        }
    }

    private String text;

    public String getText() {
        return text;
    }

    public static DMSXServerProperties valueOfStorageId(String storageId) {
        return BY_TEXT.get(storageId + ".dir_path");
    }
}
