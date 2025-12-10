package sample.application.api.shared.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sample.application.api.shared.EntityRepository;
import sample.application.api.shared.model.AbstractBaseModel;
import sample.application.api.shared.service.AbstractCrudService;
import sample.application.api.shared.util.PathUtil;

import java.net.URI;
import java.util.Objects;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/// Base class for the implementation of [RestController] that will provide only query operations
/// (to be implemented by the child classes).
/// Each child class must include the [RestController] and [RequestMapping] annotations.
/// @param <T> type of the entity that the controller will handle
///
/// @author Manoel Campos
public class AbstractSearchController<T extends AbstractBaseModel, R extends EntityRepository<T>, S extends AbstractCrudService<T, R>> {
    /**
     * Relative path of the controller in the {@link RequestMapping} annotation.
     */
    protected final String basePath;

    private final S service;

    public AbstractSearchController(final S service) {
        this.service = service;
        this.basePath = findBasePath();
    }

    protected URI createdUri(final T entity) {
        final Long id = Objects.requireNonNullElse(entity.getId(), 0L);
        return PathUtil.createUri(basePath, id.toString());
    }

    /**
     * Searches for the value of the {@link RequestMapping} annotation in the child controller class,
     * which represents the controller's path.
     *
     * @return controller path
     */
    protected String findBasePath() {
        final var handlerClass = getClass();
        final var requestMapping = findAnnotation(handlerClass, RequestMapping.class);
        return requestMapping == null ? "" : requestMapping.value()[0];
    }

    protected ResponseStatusException newNotFoundException(final long id) {
        final var msg = "%s não encontrado(a) com id " + id;
        return newNotFoundException(msg);
    }

    /**
     * Creates a resource not found exception.
     * @param msgFormat a String containing the message format, which can have a %s to be replaced by the entity name.
     * If there is no %s, the message will be displayed as passed, without formatting.
     * @return the created exception
     */
    protected ResponseStatusException newNotFoundException(final String msgFormat) {
        final var msg = msgFormat.contains("%s") ? msgFormat.formatted(service.getEntityClassName()) : msgFormat;
        return new ResponseStatusException(NOT_FOUND, msg);
    }

    protected R getRepository() {
        return service.getRepository();
    }

    public S getService() {
        return service;
    }
}
