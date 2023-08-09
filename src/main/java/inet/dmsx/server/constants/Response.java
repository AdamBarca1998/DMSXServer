package inet.dmsx.server.constants;

import io.undertow.util.StatusCodes;

public enum Response {

    PAGE_NOT_FOUND("Page Not Found", StatusCodes.NOT_FOUND),
    OK("OK", StatusCodes.OK);

    Response(String text, int code) {
        this.text = text;
        this.code = code;
    }

    private String text;
    private int code;

    public String getText() {
        return text;
    }

    public int getCode() {
        return code;
    }
}
