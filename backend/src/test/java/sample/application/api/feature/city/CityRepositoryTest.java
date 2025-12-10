package sample.application.api.feature.city;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import sample.application.api.feature.AbstractRepositoryTest;
import sample.application.api.feature.district.District;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private CityRepository repository;
    private City instance;

    @BeforeEach
    void setUp() {
        this.instance = new City("City 1", new District(1));
        repository.save(instance);
    }

    @AfterEach
    void tearDown() {
        repository.delete(instance);
    }

    @Test
    void findById() {
        assertNotNull(instance.getId());
        assertTrue(repository.findById(instance.getId()).isPresent());
    }

    @Test
    void deleteById() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            repository.deleteById(1L);
            repository.flush();
        });

        //assertConstraintViolation(ex, ConstraintKeys.FK_CIDADE__ESTADO);
    }

    @Test
    void findFirstByDescricao() {
        final var description = "Palmas";
        final List<City> list = repository.findByNameLike(description);
        assertEquals(1, list.size());
        assertEquals(description, list.getFirst().name);
    }

    @Test
    void inserirDescricaoDuplicadaGeraExcecao() {
        final var city = new City(instance.name, new District(1));
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(city));
    }
}
