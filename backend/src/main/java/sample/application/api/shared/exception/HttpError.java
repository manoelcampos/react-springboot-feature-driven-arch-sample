package sample.application.api.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Represents an HTTP error message with its corresponding status code.
 * @param status HTTP status code
 * @param description Description of the HTTP status code
 * @param message Error message
 * @author Manoel Campos
 */
public record HttpError(int status, String description, String message) {
    public HttpError(final ResponseStatusException ex) {
        this(ex.getStatusCode().value(), getHttpStatusName(ex), ex.getReason());
    }

    public HttpError(final HttpStatus status, final String message) {
        this(status.value(), status.name(), message);
    }

    /**
     * {@return the http status name from an HTTP error message}
     * If the error message is "404 Not Found", it returns "Not Found".
     * @param ex the exception containing the HTTP error message
     */
    private static String getHttpStatusName(final ResponseStatusException ex) {
        final String[] errorMsgParts = ex.getMessage().split(" ");
        if(errorMsgParts.length < 2)
            return "";

        return errorMsgParts[1];
    }
}
