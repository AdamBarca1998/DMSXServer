package inet.dmsx.server.constants;

import io.undertow.util.StatusCodes;

public enum Response {

    ILLEGAL_STATE_SERVER("Illegal state server!", StatusCodes.METHOD_NOT_ALLOWED),
    ERROR("Something wrong", StatusCodes.INTERNAL_SERVER_ERROR),
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
