package sample.application.api.shared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import sample.application.api.shared.model.BaseModel;

/// Provides a contract for creating other interfaces annotated with
/// [org.springframework.stereotype.Repository]
/// and that handle entities of type [BaseModel].
/// The implementation of such interfaces is automatically created by Spring Data JPA
/// and instantiated through dependency injection.
/// @author Manoel Campos
@NoRepositoryBean
public interface EntityRepository<T extends BaseModel> extends JpaRepository<T, Long> {
}
