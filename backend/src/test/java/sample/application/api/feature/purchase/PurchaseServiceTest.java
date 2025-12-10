package sample.application.api.feature.purchase;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    /**
     * @InjectMocks instancia o serviço,
     * mas pegando os objetos fake anotados com
     * @Mock e armazenando eles nos atributos
     * de mesmo tipo dentro do serviço.
     * Assim, o serviço não vai usar um objeto
     * real para tais atributos, mas o
     * fantoche (mock) que criamos.
     */
    @InjectMocks
    private PurchaseService service;

    /**
     * @Autowired is only used to instantiate real objects
     * @Mock creates a fake object, a puppet that you define how it will behave
     */
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
    void insertProdutoSemEstoque() {
        configurarMockProdutoRepository(prod1);
        final var itens = List.of(new PurchaseItem(1, 20), new PurchaseItem(2, 5));
        purchase.setItens(itens);

        assertThrows(IllegalStateException.class, () -> service.save(purchase));
    }

    @Test
    void insertProdutoNaoInformado() {
        configurarMockProdutoRepository(prod1);
        final var itens = List.of(new PurchaseItem(1, 2), new PurchaseItem());
        purchase.setItens(itens);

        assertThrows(IllegalStateException.class, () -> service.save(purchase));
    }

    @Test
    void insertIdProdutoNaoInformado() {
        configurarMockProdutoRepository(prod1);
        final var itens = List.of(new PurchaseItem(1, 2), new PurchaseItem(new Product()));
        purchase.setItens(itens);
        assertThrows(IllegalStateException.class, () -> service.save(purchase));
    }

    @Test
    void insertProdutoNaoLocalizado() {
        final var itens = List.of(new PurchaseItem(3, 2));
        purchase.setItens(itens);
        assertThrows(NoSuchElementException.class, () -> service.save(purchase));
    }

    @Test
    void updateVendaNaoAlteraEstoqueProduto() {
        final var itens = List.of(new PurchaseItem(1, 2), new PurchaseItem());
        final var vendaExistente = new Purchase(4, itens);
        Mockito.when(repository.saveAndFlush(vendaExistente)).thenReturn(vendaExistente);

        final var vendaObtida = service.save(vendaExistente);
        assertEquals(vendaExistente, vendaObtida);
    }

}
