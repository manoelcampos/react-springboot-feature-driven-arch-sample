package sample.application.api.feature.product;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.application.api.shared.controller.AbstractController;

@RestController
@RequestMapping("/product")
public class ProductController extends AbstractController<Product, ProductDTO, ProductRepository, ProductService> {
    public ProductController(final ProductService service) {
        super(ProductDTO.class, service);
    }
}
