package sample.application.api.feature.product;

import org.springframework.stereotype.Service;
import sample.application.api.shared.service.AbstractCrudService;

@Service
public class ProductService extends AbstractCrudService<Product, ProductRepository> {
    public ProductService(final ProductRepository repository) {
        super(repository);
    }

    @Override
    public boolean deleteById(final long id) {
        getRepository().findById(id).ifPresent(prod -> {
            if(prod.hasInventory())
                throw new IllegalStateException("Product %s cannot be deleted because it is still in stock.".formatted(prod.description));
        });

        return super.deleteById(id);
    }
}
