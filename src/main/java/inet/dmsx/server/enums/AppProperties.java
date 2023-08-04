package inet.dmsx.server.enums;

public enum AppProperties {

    HOST("host"),
    PORT("port"),
    FILE_PATH("file_path");

    AppProperties(String text) {
        this.text = text;
    }

    private String text;

    public String getText() {
        return text;
    }
}
