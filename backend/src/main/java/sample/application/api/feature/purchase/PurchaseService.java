package sample.application.api.feature.purchase;

import org.springframework.stereotype.Service;
import sample.application.api.feature.product.Product;
import sample.application.api.feature.product.ProductRepository;
import sample.application.api.shared.service.AbstractCrudService;

import java.util.NoSuchElementException;

import static java.util.Objects.requireNonNullElse;

@Service
public class PurchaseService extends AbstractCrudService<Purchase, PurchaseRepository> {
    private final ProductRepository productRepository;

    public PurchaseService(final PurchaseRepository repository, final ProductRepository productRepository) {
        super(repository);
        this.productRepository = productRepository;
    }

    /**
     * The method does not take into account that concurrent customers may be
     * buying the same product at the same time, and therefore,
     * the implementation may end up allowing the stock to become negative
     * in these cases.
     */
    @Override
    public Purchase save(final Purchase purchase) {
        verificarEstoque(purchase);
        return super.save(purchase);
    }

    private void verificarEstoque(final Purchase purchase) {
        if(purchase.isEditing())
            return;

        for (PurchaseItem item : purchase.itens) {
            final var produto = item.product;

            final Long prodId = requireNonNullElse(produto, new Product()).id;
            if (prodId == null) {
                throw new IllegalStateException("Product not specified");
            }

            final var prod =
                    productRepository
                            .findById(prodId)
                            .orElseThrow(() -> new NoSuchElementException("Product not found"));
            if(prod.isInventoryEnough(item)){
                throw new IllegalStateException("Product %s is out of stock.".formatted(prod.description));
            }
        }
    }
}
