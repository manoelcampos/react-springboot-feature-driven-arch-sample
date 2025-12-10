package sample.application.api.feature.district;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.application.api.shared.controller.AbstractController;

@RestController
@RequestMapping("/district")
public class DistrictController extends AbstractController<District, DistrictDTO, DistrictRepository, DistrictService> {
    public DistrictController(final DistrictService service) {
        super(DistrictDTO.class, service);
    }
}
