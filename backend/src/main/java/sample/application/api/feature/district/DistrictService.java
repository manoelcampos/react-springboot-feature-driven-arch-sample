package sample.application.api.feature.district;

import org.springframework.stereotype.Service;
import sample.application.api.shared.service.AbstractCrudService;

@Service
public class DistrictService extends AbstractCrudService<District, DistrictRepository> {
    public DistrictService(final DistrictRepository repository) {
        super(repository);
    }

    @Override
    public District save(final District district) {
        if(district.name.equalsIgnoreCase(district.abbreviation))
            throw new IllegalStateException("The country district description cannot be equal to its abbreviation");

        return super.save(district);
    }
}
