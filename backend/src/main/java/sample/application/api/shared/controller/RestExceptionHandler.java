package sample.application.api.shared.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import sample.application.api.shared.exception.HttpError;
import sample.application.api.shared.model.AbstractBaseModel;
import sample.application.api.shared.util.ConstraintViolation;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CONFLICT;
import static sample.application.api.shared.util.ConstraintViolation.findUniqueConstraintMessage;

/**
 * Catches specific exceptions to return an {@link HttpError}
 * with a user-friendly message.
 *
 * @author Manoel Campos
 */
@ControllerAdvice
public class RestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /// Captures [ResponseStatusException] exceptions
    /// to allow sending a message along with the status code.
    /// Since [AbstractController] are generic, it is not possible to return
    /// a String in the message body, but rather an [AbstractBaseModel] object.
    /// Therefore, the exception needs to be caught here and returned as a [ResponseEntity]
    /// String with the custom error message.
    ///
    /// @param ex captured exception
    /// @return [ResponseEntity] with the custom error message
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<HttpError> handleResponseStatusException(final ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(new HttpError(ex));
    }

    /// Captures [DataIntegrityViolationException] exceptions
    /// to check if they were thrown due to a Foreign Key violation
    /// @param ex thrown exception
    /// @return [ResponseEntity] with the custom error message
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<HttpError> handleDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        final var status = CONFLICT;
        final var msg = ConstraintViolation
                            .findForeignKeyMessage(ex)
                            .or(() -> findUniqueConstraintMessage(ex))
                            .orElse("Error executing operation");

        return ResponseEntity.status(status).body(new HttpError(status, msg));
    }

    /// Catches a validation error from Hibernate Validator that occurs
    /// when a parameter of an endpoint in a controller is annotated with [Valid].
    ///
    /// @param ex occurred exception
    /// @return [ResponseEntity] with the custom error message
    @ExceptionHandler(BindException.class)
    public ResponseEntity<HttpError> handleResponseStatusException(final BindException ex) {
        final String errorMessages = getValidationErrorMessages(ex);
        var statusEx = newConflictException(errorMessages);
        return ResponseEntity.status(statusEx.getStatusCode()).body(new HttpError(statusEx));
    }

    private static String getValidationErrorMessages(final BindException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                 .map(RestExceptionHandler::getValidationMessage)
                 .collect(Collectors.joining(". "));
    }

    private static String getValidationMessage(final ObjectError error) {
        final var target = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
        return "%s %s".formatted(target, error.getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpError> handleResponseStatusException(final Exception ex) {
        final var msg = "An unexpected error occurred";
        logger.error(msg, ex);
        final var status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(new HttpError(status.value(), status.name(), msg));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<HttpError> handleIllegalStateException(final IllegalStateException ex) {
        return validationException(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpError> handleIllegalArgumentException(final IllegalArgumentException ex) {
        return validationException(ex);
    }

    private static ResponseEntity<HttpError> validationException(final Exception ex) {
        final var msg = ex.getMessage();
        logger.error("Validation error", ex);
        final var status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new HttpError(status.value(), status.name(), msg));
    }

    public static ResponseStatusException newConflictException(final String msg) {
        return new ResponseStatusException(CONFLICT, msg);
    }
}
