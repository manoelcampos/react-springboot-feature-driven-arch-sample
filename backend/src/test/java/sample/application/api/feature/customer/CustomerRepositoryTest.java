package sample.application.api.feature.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import sample.application.api.feature.AbstractRepositoryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private CustomerRepository repository;

    @Test
    void findByCityId() {
        final var customerList = repository.findByCityId(1);
        assertEquals(2, customerList.size());
    }

    @ParameterizedTest
    @CsvSource({"Ana%,1", "%Silva,2", "%Pedro%,3"})
    void findByNameLike(final String nome, final int count) {
        final var customerList = repository.findByNameLike(nome);
        assertEquals(count, customerList.size());
    }
}
