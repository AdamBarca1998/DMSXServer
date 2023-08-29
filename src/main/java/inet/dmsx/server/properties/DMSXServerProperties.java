package inet.dmsx.server.properties;

public enum DMSXServerProperties {

    HOST("host"),
    PORT("port"),
    DIR_PATH(".dir_path"),
    DELETE_OLDER_HRS(".delete_older_hrs"),
    FILE_PATH(".file_path"),
    STORAGE_IDS("storageIds")
    ;

    DMSXServerProperties(String text) {
        this.text = text;
    }

    private final String text;

    public String getText() {
        return text;
    }
}
