package sample.application.api.shared.service;

import org.springframework.stereotype.Service;
import sample.application.api.shared.EntityRepository;
import sample.application.api.shared.controller.AbstractController;
import sample.application.api.shared.model.AbstractBaseModel;

import java.util.List;
import java.util.Optional;

/// Provides a contract for the implementation of [Service]s that
/// perform CRUD operations on an [AbstractController].
/// Encapsulates all business rules, leaving the controller
/// to handle only the HTTP layer
/// (such as response, request, status codes, redirect, etc).
/// @param <T> the type of entity that the service will manage
/// @param <R> the type of repository for accessing the entity's data in the database
///
/// @author Manoel Campos
public interface CrudService<T extends AbstractBaseModel, R extends EntityRepository<T>> {
    boolean deleteById(long id);

    Optional<T> findById(long id);

    List<T> findAll();

    T save(T entity);

    /// {@return the name of the entity that the service handles}
    /// This class is the [AbstractBaseModel] defined generically
    /// in the class declarations that implement this interface.
    String getEntityClassName();
}
