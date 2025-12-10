package sample.application.api.shared.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.annotation.RequestScope;
import sample.application.api.shared.model.AbstractBaseModel;
import sample.application.api.shared.service.CrudService;

/// Defines a contract for implementing custom [Validator]s.
/// It is defined as a concrete class (instead of an interface)
/// just to allow dependency injection of a validator for a specific [AbstractBaseModel],
/// when a subclass of this one is not created,
/// indicating that the entity has no custom validations.
///
/// In these cases, Spring can instantiate an object of this class (since it is concrete).
/// This way, there is no need to store
/// null in a validator object of a [CrudService]
/// and thus no need to write checks to avoid NullPointerExceptions.
/// Even if there is no class to validate a specific entity,
/// the service class of that entity will have an instance of a default validator
/// (which does not perform any custom validation).
///
/// **WARNING**: Concrete subclasses should not be created from this class,
/// but rather from [AbstractCustomValidator], as that class will provide
/// part of the implementation.
///
/// @author Manoel Campos
@Component @RequestScope
public class CustomValidator<T extends AbstractBaseModel> implements Validator {
    /// @return the type of [AbstractBaseModel] this validator can validate.
    @SuppressWarnings("unchecked")
    protected Class<T> getSupportedClass(){
        // The method must be overridden by subclasses to use a specific type of entity.
        return  (Class<T>) AbstractBaseModel.class;
    }

    @Override
    public final boolean supports(final Class<?> clazz) {
        return getSupportedClass().isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        /*
         Empty implementation that does not perform any custom validation so that subclasses are
         not forced to implement this method. It can be implemented only if a desired subclass needs such a behaviour.
        */
    }
}
