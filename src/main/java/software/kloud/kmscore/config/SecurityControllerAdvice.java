package software.kloud.kmscore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SecurityControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(SecurityControllerAdvice.class);

    @ExceptionHandler({SecurityException.class})
    public ResponseEntity<String> handleSecurityException(SecurityException e) {
        log.error("Security exception", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incorrect auth header present");
    }
}
