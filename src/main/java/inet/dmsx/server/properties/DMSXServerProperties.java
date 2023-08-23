package inet.dmsx.server.properties;

import java.util.HashMap;
import java.util.Map;

public enum DMSXServerProperties {

    HOST("host"),
    PORT("port"),
    DIR_PATH(".dir_path"),
    DELETE_OLDER_HRS(".delete_older_hrs"),
    FILE_PATH(".file_path")
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
}
