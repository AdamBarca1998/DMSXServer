package inet.dmsx.server.enums;

public enum ResponseString {

    Page_Not_Found("Page Not Found"),
    SOMETHING_WRONG("Something wrong!"),
    OK("OK");

    ResponseString(String text) {
        this.text = text;
    }

    private String text;

    public String getText() {
        return text;
    }
}
