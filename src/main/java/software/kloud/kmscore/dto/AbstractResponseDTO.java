package software.kloud.kmscore.dto;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;

public class AbstractResponseDTO {
    private List<RequestError> requestErrors = new ArrayList<>();

    public <T> void addRequestError(ConstraintViolation<T> violation) {
        var msg = violation.getMessage();
        var name = violation.getPropertyPath().toString();

        this.requestErrors.add(new RequestError(name, msg));
    }

    public void addRequestError(String field, String msg) {
        this.requestErrors.add(new RequestError(field, msg));
    }

    public List<RequestError> getRequestErrors() {
        return requestErrors;
    }
}
