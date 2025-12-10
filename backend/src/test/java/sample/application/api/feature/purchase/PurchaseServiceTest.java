package sample.application.api.feature.purchase;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import sample.application.api.feature.customer.Customer;
import sample.application.api.feature.product.AbstractServiceTest;
import sample.application.api.feature.product.Product;
import sample.application.api.feature.product.ProductRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PurchaseServiceTest extends AbstractServiceTest {
    /// [InjectMocks] instantiates the service,
    /// but it takes the fake objects annotated with
    /// [Mock] and stores them in the attributes
    /// of the same type within the service.
    /// This way, the service won't use a real object
    /// for such attributes, but the mock we created.
    @InjectMocks
    private PurchaseService service;

    /// [Autowired] is only used to instantiate real objects
    /// [Mock] creates a fake object, a puppet that you define how it will behave
    @Mock
    private ProductRepository productRepository;

    @Mock
    private PurchaseRepository repository;

    private final Purchase purchase = new Purchase(new Customer(1));
    private final Product prod1 = new Product(1, "Prod 1", 100.0, 10);

    private void configurarMockProdutoRepository(Product prod) {
        final Long id = Objects.requireNonNullElse(prod.getId(), 0L);
        // Mockito is the mock library included in Spring.
        Mockito
            .when(productRepository.findById(id))
            .thenReturn(Optional.of(prod));
    }

    @Test
    void insertOutOfStockProduct() {
        configurarMockProdutoRepository(prod1);
        final var itens = List.of(new PurchaseItem(1, 20), new PurchaseItem(2, 5));
        purchase.setItens(itens);

        assertThrows(IllegalStateException.class, () -> service.save(purchase));
    }

    @Test
    void insertWithProductNotDefined() {
        configurarMockProdutoRepository(prod1);
        final var itens = List.of(new PurchaseItem(1, 2), new PurchaseItem());
        purchase.setItens(itens);

        assertThrows(IllegalStateException.class, () -> service.save(purchase));
    }

    @Test
    void insertWithProductIdNotDefined() {
        configurarMockProdutoRepository(prod1);
        final var itens = List.of(new PurchaseItem(1, 2), new PurchaseItem(new Product()));
        purchase.setItens(itens);
        assertThrows(IllegalStateException.class, () -> service.save(purchase));
    }

    @Test
    void insertWithProductNotFound() {
        final var itens = List.of(new PurchaseItem(3, 2));
        purchase.setItens(itens);
        assertThrows(NoSuchElementException.class, () -> service.save(purchase));
    }

    @Test
    void updatePurchaseDoesntChangeProductStock() {
        final var itens = List.of(new PurchaseItem(1, 2), new PurchaseItem());
        final var existingPurchase = new Purchase(4, itens);
        Mockito.when(repository.saveAndFlush(existingPurchase)).thenReturn(existingPurchase);

        final var fetchedPurchase = service.save(existingPurchase);
        assertEquals(existingPurchase, fetchedPurchase);
    }

}
