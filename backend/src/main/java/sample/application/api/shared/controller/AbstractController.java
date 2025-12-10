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

/**
 * Classe base para a implementação de {@link RestController} que fornecem todas as operações CRUD
 * e podem trabalhar tanto com entidades (classes model) quanto com DTOs.
 * Se um DTO for passado no parâmetro D, os métodos {@link #insert(Object)}
 * e {@link #update(long, Object)} irão receber um DTO no lugar de uma entidade correspondente.
 *
 * <p>Cada classe filha deve incluir a anotação {@link RestController} e {@link RequestMapping}.</p>
 *
 * @param <T> tipo da entidade que o controller irá manipular
 * @param <D> tipo do DTO para a entidade entidade que o controller irá manipular.
 *            Se D for o mesmo tipo que T, o controller não irá trabalhar com DTOs nos métodos citados acima.
 * @param <R> tipo do repositório que acesso os dados da entidade no banco
 * @author Manoel Campos
 */
public abstract class AbstractController<T extends AbstractBaseModel, D, R extends EntityRepository<T>, S extends AbstractCrudService<T, R>> extends AbstractSearchController<T, R, S> {
    /**
     * Validador customizado para a entidade manipulada pelo controller.
     * O validador é opcional, pois nem sempre é necessário
     * realizar validações customizadas para a entidade.
     * Se nenhuma classe validator para a entidade for definida, uma instância de {@link CustomValidator} é usada.
     */
    @Autowired
    private CustomValidator<T> validator;
    private final Class<D> dtoClass;

    /**
     * Uma instância de DTORecord vazia, apenas para permitir chamar o método {@link DTORecord#fromModel(Object)}.
     * Como a instanciação de tal objeto exige reflection, é feita a instanciação uma única
     * vez no construtor do controller, para não penalizar o desempenho de endpoints que precisam dessa instância.
     *
     * @see #findDtoById(long)
     */
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
        Se o emptyDto for null, é porque o tipo genérico D é, na verdade, ume entity T.
        Isto indica que o controller não trabalha com DTOs, mas somente com entidades.
        Assim, fazer um cast de model (do tipo T) para D não irá funcionar (pois D é o mesmo tipo T).
        */
        return ResponseEntity.ok((D) (emptyDto == null ? model : emptyDto.fromModel(model)));
    }

    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        return ResponseEntity.ok(getService().findAll());
    }

    /**
     * Insere um objeto como um novo registro no banco de dados.
     *
     * @param obj objeto que pode ser uma entidade do tipo T ou um DTORecord.
     * @return
     */
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

    /**
     * Atualiza um registro no banco de dados a partir dos dados de um objeto.
     *
     * @param obj objeto que pode ser uma entidade do tipo T ou um DTORecord.
     * @return
     */
    @PutMapping("{id}")
    @Transactional
    public void update(@Valid @PathVariable final long id, @Valid @RequestBody final D obj) {
        final T entity = getEntity(obj);
        if (!entity.isSameId(id)) {
            final var msg = "O ID informado (%d) não corresponde com o ID do %s (%d)".formatted(id, getService().getEntityClassName(), entity.getId());
            throw newConflictException(msg);
        }

        validate(entity);

        try {
            getService().save(entity);
        } catch (final ConstraintViolationException e) {
            throw newConflictException(e.getMessage());
        }
    }

    /**
     * Tenta converter um objeto D para o tipo genérico T que representa uma entidade gerenciada
     * pelo service.
     *
     * @param obj objeto para tentar converter para uma entidade.
     *            Tal objeto pode já ser uma entidade do tipo T ou um DTORecord.
     * @return
     */
    @SuppressWarnings("unchecked")
    private T getEntity(final D obj) {
        if (obj instanceof DTORecord)
            return ((DTORecord<T>) obj).toModel();

        try {
            return (T) obj;
        } catch (final ClassCastException e) {
            throw new RuntimeException(
                    "Os objetos gerenciados pelo %s devem ser do tipo T ou %s"
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

    /**
     * Cria um DTORecord vazio, apenas para permitir chamar o método {@link DTORecord#fromModel(Object)}
     * para permitir criar posteriormente um DTO a partir de uma {@link BaseModel}.
     * @return o DTORecord vazio ou null se o tipo genérico D não for um {@link DTORecord}.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    private DTORecord<T> newEmptyDtoRecord(){
        if(!DTORecord.class.isAssignableFrom(dtoClass))
            return null;

        try {
            final var constructor = dtoClass.getDeclaredConstructor();
            return (DTORecord<T>)constructor.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Erro tentando instanciar %s".formatted(dtoClass.getName()), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Erro tentando acessar o construtor de %s".formatted(dtoClass.getName()), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exceção executando o construtor de %s".formatted(dtoClass.getName()), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Nenhum construtor sem parâmetros encontrado para o DTO " + dtoClass.getName(), e);
        }
    }
}
