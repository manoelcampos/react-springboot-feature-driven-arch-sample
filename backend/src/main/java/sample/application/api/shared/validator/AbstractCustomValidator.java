package sample.application.api.shared.validator;

import org.springframework.validation.Errors;
import sample.application.api.shared.model.AbstractBaseModel;

/// Base class for implementing [CustomValidator]s.
/// @author Manoel Campos
public abstract class AbstractCustomValidator<T extends AbstractBaseModel> extends CustomValidator<T> {
    @Override
    protected abstract Class<T> getSupportedClass();

    @SuppressWarnings("unchecked") @Override
    public final void validate(final Object target, final Errors errors) {
        final var supportedClass = getSupportedClass().getSimpleName();
        final var targetClass = target.getClass();
        if(!supports(targetClass)) {
            final var validatorClass = getClass().getSimpleName();
            final var format = "%s only allows validation of objects of type %s. Attempting to validate %s";
            final var msg = format.formatted(validatorClass, supportedClass, targetClass.getSimpleName());
            throw new UnsupportedOperationException(msg);
        }

        validateInternal((T)target, errors);
    }

    /**
     * Implements the specific validation rules for the entity.
     * @param target object to be validated
     * @param errors errors captured during validation
     * @see #validate(Object, Errors)
     */
    protected abstract void validateInternal(T target, Errors errors);
}
