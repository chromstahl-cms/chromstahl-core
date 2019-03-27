package software.kloud.kmscore.dto;

public class RequestError {
    private final String field;
        private final String errorMsg;

    public RequestError(String field, String errorMsg) {
        this.field = field;
        this.errorMsg = errorMsg;
    }

    public String getField() {
        return field;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
