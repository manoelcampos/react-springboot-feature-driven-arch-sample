package sample.application.api.feature.product;

import org.springframework.stereotype.Repository;
import sample.application.api.shared.EntityRepository;

import java.util.List;

@Repository
public interface ProductRepository extends EntityRepository<Product> {
    List<Product> findByDescriptionLike(String description);
}
