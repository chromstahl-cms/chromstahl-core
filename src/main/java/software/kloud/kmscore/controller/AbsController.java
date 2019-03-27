package software.kloud.kmscore.controller;

import software.kloud.kmscore.dto.TokenResponseDTO;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class AbsController {
    private final static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    <T> boolean checkForViolations(TokenResponseDTO respDTO, T entity) {
        Set<ConstraintViolation<T>> violations = factory.getValidator().validate(entity);
        if (!violations.isEmpty()) {
            violations.forEach(respDTO::addRequestError);
            return true;
        }
        return false;
    }
}
