package sample.application.api.shared.service;

import org.springframework.stereotype.Service;
import sample.application.api.shared.EntityRepository;
import sample.application.api.shared.controller.AbstractController;
import sample.application.api.shared.model.AbstractBaseModel;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

/// Base implementation of a [CrudService] to
/// perform CRUD operations on a [AbstractController].
/// @param <T> {@inheritDoc}
/// @param <R> {@inheritDoc}
/// @author Manoel Campos
@Service
public abstract class AbstractCrudService<T extends AbstractBaseModel, R extends EntityRepository<T>> implements CrudService<T, R> {
    private final R repository;
    private final String entityClassName;

    protected AbstractCrudService(final R repository) {
        this.repository = repository;
        final var typeParameters = repository.getClass().getTypeParameters();
        entityClassName = typeParameters.length == 0 ? "Object" : typeParameters[0].getClass().getSimpleName();
    }

    /// Obtains a [Supplier] of [NoSuchElementException] with the message passed as a parameter.
    /// This method can be called in operations like [Optional#orElseThrow(Supplier)]
    /// when calling methods like [EntityRepository#findById(Object)] and any
    /// other that returns an Optional.
    /// @param msg error message to be displayed when the exception returned by the Supplier is thrown
    /// @return the [NoSuchElementException] supplier
    protected static Supplier<NoSuchElementException> notFoundSupplier(final String msg) {
        return () -> new NoSuchElementException(msg);
    }

    @Override
    public boolean deleteById(final long id) {
        return findById(id).map(this::deleteEntity).orElse(false);
    }

    private boolean deleteEntity(final T entity){
        repository.delete(entity);
        return true;
    }

    @Override
    public Optional<T> findById(final long id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T save(final T entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public String getEntityClassName() {
        return entityClassName;
    }

    public R getRepository() {
        return repository;
    }
}
