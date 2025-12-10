package sample.application.api.shared.controller;

import io.github.manoelcampos.dtogen.DTORecord;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sample.application.api.shared.EntityRepository;
import sample.application.api.shared.model.AbstractBaseModel;
import sample.application.api.shared.model.BaseModel;
import sample.application.api.shared.service.AbstractCrudService;
import sample.application.api.shared.validator.CustomValidator;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CONFLICT;
import static sample.application.api.shared.controller.RestExceptionHandler.newConflictException;

/// Base class for implementing [RestController] that provide all CRUD operations
/// and can work with both entities (model classes) and DTOs.
/// If a DTO is passed in parameter D, the methods [#insert(Object)]
/// and [#update(long,Object)] will receive a DTO instead of a corresponding entity.
///
/// Each child class must include the annotation [RestController] and [RequestMapping].
///
/// @param <T> type of the entity that the controller will handle
/// @param <D> type of the DTO for the entity that the controller will handle.
/// If D is the same type as T, the controller will not work with DTOs in the aforementioned methods.
/// @param <R> type of the repository that accesses the entity data in the database
/// @author Manoel Campos
public abstract class AbstractController<T extends AbstractBaseModel, D, R extends EntityRepository<T>, S extends AbstractCrudService<T, R>> extends AbstractSearchController<T, R, S> {
    /// Custom validator for the entity handled by the controller.
    /// The validator is optional, as it’s not always necessary
    /// to perform custom validations for the entity.
    /// If no validator class for the entity is defined, an instance of [CustomValidator] is used.
    @Autowired
    private CustomValidator<T> validator;
    private final Class<D> dtoClass;

    /// An empty [DTORecord] instance, just to allow calling the [DTORecord#fromModel(Object)] method.
    /// Since instantiating such an object requires reflection, the instantiation is done only once
    /// in the controller's constructor, so as not to impact the performance of endpoints that need this instance.
    ///
    /// @see #findDtoById(long)
    @Nullable
    private final DTORecord<T> emptyDto;

    public AbstractController(final Class<D> dtoClass, final S service) {
        super(service);
        this.dtoClass = dtoClass;
        this.emptyDto = newEmptyDtoRecord();
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<T> delete(@Valid @PathVariable final long id) {
        if (getService().deleteById(id))
            return ResponseEntity.noContent().build();

        throw newNotFoundException(id);
    }

    @GetMapping("{id}")
    public ResponseEntity<T> findById(@Valid @PathVariable final long id) {
        return getService().findById(id)
                      .map(ResponseEntity::ok)
                      .orElseThrow(() -> newNotFoundException(id));
    }

    @GetMapping("/dto/{id}")
    @SuppressWarnings("unchecked")
    public ResponseEntity<D> findDtoById(@Valid @PathVariable final long id) {
        final T model = getService().findById(id).orElseThrow(() -> newNotFoundException(id));

        /*
        If emptyDto is null, it means that the generic type `D` is actually an entity `T`.
        This indicates that the controller does not work with DTOs, but only with entities.
        Therefore, casting the model (of type `T`) to `D` will not work (since `D` is the same type `T`).
        */
        return ResponseEntity.ok((D) (emptyDto == null ? model : emptyDto.fromModel(model)));
    }

    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        return ResponseEntity.ok(getService().findAll());
    }

    /// Inserts an object as a new record in the database.
    ///
    /// @param obj object that can be an entity of type T or a [DTORecord].
    /// @return
    @PostMapping
    @Transactional
    public ResponseEntity<T> insert(@Valid @RequestBody D obj) {
        T entity = getEntity(obj);

        validate(entity);
        try {
            entity.id = null;
            entity = getService().save(entity);
            return ResponseEntity.created(createdUri(entity)).body(entity);
        } catch (final ConstraintViolationException e) {
            throw newConflictException(e.getMessage());
        }
    }

    /// Updates a record in the database using data from an object.
    ///
    /// @param obj object that can be an entity of type T or a [DTORecord].
    /// @return
    @PutMapping("{id}")
    @Transactional
    public void update(@Valid @PathVariable final long id, @Valid @RequestBody final D obj) {
        final T entity = getEntity(obj);
        if (!entity.isSameId(id)) {
            final var msg = "The provided ID (%d) does not match the %s ID (%d)".formatted(id, getService().getEntityClassName(), entity.getId());
            throw newConflictException(msg);
        }

        validate(entity);

        try {
            getService().save(entity);
        } catch (final ConstraintViolationException e) {
            throw newConflictException(e.getMessage());
        }
    }

    /// Attempts to convert an object D to the generic type T, which represents an entity managed
    /// by the service.
    ///
    /// @param obj object to try to convert to an entity.
    /// Such an object may already be an entity of type T or a [DTORecord].
    /// @return
    @SuppressWarnings("unchecked")
    private T getEntity(final D obj) {
        if (obj instanceof DTORecord)
            return ((DTORecord<T>) obj).toModel();

        try {
            return (T) obj;
        } catch (final ClassCastException e) {
            throw new RuntimeException(
                    "The objects managed by %s must be of type T or %s"
                    .formatted(getClass().getSimpleName(), DTORecord.class.getSimpleName()), e);
        }
    }

    private void validate(final T entity) {
        final var errors = new BindException(entity, entity.getClass().getSimpleName());
        validator.validate(entity, errors);
        final var errorsStr = errors.getAllErrors()
                                    .stream()
                                    .map(DefaultMessageSourceResolvable::getCode)
                                    .collect(Collectors.joining(";\n"));
        if (errors.hasErrors())
            throw new ResponseStatusException(CONFLICT, errorsStr);
    }

    /// Creates an empty [DTORecord], just to allow calling the method [DTORecord#fromModel(Object)]
    /// to later create a DTO from a [BaseModel].
    /// @return the empty DTORecord or null if the generic type D is not a [DTORecord].
    @SuppressWarnings("unchecked")
    @Nullable
    private DTORecord<T> newEmptyDtoRecord(){
        if(!DTORecord.class.isAssignableFrom(dtoClass))
            return null;

        try {
            final var constructor = dtoClass.getDeclaredConstructor();
            return (DTORecord<T>)constructor.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Error trying to instantiate %s".formatted(dtoClass.getName()), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error trying to access the constructor of %s".formatted(dtoClass.getName()), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exception executing the constructor of %s".formatted(dtoClass.getName()), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No parameterless constructor found for the " + dtoClass.getName(), e);
        }
    }
}
