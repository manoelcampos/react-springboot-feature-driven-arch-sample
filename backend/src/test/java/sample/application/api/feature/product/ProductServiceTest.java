package sample.application.api.feature.product;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTest extends AbstractServiceTest {
    /**
     * Creates a real service object and injects into it the mocks created with the @Mock annotation.
     */
    @InjectMocks
    private ProductService service;

    @Mock // Creates a fake object (a puppet) to simulate the behavior of a real object.
    private ProductRepository repository;

    @Test
    void deleteById() {
        final long id = 1L;
        final var prod = new Product(id, "Produto 1", 10.0, 5);
        Mockito
                .when(repository.findById(id))
                .thenReturn(Optional.of(prod));

        assertThrows(IllegalStateException.class, () -> service.deleteById(id));
    }
}
