package sample.application.api.feature.district;

import org.springframework.stereotype.Repository;
import sample.application.api.shared.EntityRepository;

import java.util.List;

@Repository
public interface DistrictRepository extends EntityRepository<District> {
    List<District> findByNameLike(String name);
}
