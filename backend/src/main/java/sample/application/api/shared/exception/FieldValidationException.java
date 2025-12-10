package sample.application.api.shared.exception;

/**
 * An exception about the validation of a field.
 * @author Manoel Campos
 */
public class FieldValidationException extends RuntimeException {
    private final String fieldName;

    /**
     * Creates a field validation exception.
     * @param message validation error message
     * @param fieldName name of the field being validated
     */
    public FieldValidationException(final String message, final String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }
}
