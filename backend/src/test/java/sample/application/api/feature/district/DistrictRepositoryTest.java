package sample.application.api.feature.district;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/// The [DataJpaTest] annotation is used for repository tests.
/// It will create a connection to the database
/// and allow testing of the repositories.
@DataJpaTest
class DistrictRepositoryTest {
    public static final String NOME_ESPERADO = "Novo Estado";

    /// [Autowired] annotation to inject the repository instance.
    /// That is, it creates an instance of the object for us.
    @Autowired
    private DistrictRepository repository;

    @Test
    void findById() {
        final long id = 1;
        final var expectedName = "São Paulo";
        findById(id, expectedName);
    }

    private void findById(long id, String nomeEsperado) {
        District district = repository.findById(id).orElseThrow();
        assertEquals(nomeEsperado, district.name);
    }

    @Test
    void successfulInsert() {
        final long id = insertState();
        System.out.println(id);
        assertNotEquals(0, id);
        findById(id, NOME_ESPERADO);
    }

    /**
     * Inserts a district into the database
     * @return the ID of the inserted district
     * @throws ConstraintViolationException if the district data validation fails and the district is not inserted
     */
    private long insertState() {
        final var estado = new District();
        estado.name = NOME_ESPERADO;
        estado.abbreviation = "NE";
        final var novoEstado = repository.save(estado);

        /* Uses requireNonNullElse to return zero if the ID is null.
         * But this is just a check so the IDE doesn't complain elsewhere
         * that the ID might be null.
         * The ID would only be null if the district hasn't been inserted, which will cause an error on the line
         * above, and it wouldn't even reach here. */
        return Objects.requireNonNullElse(novoEstado.getId(), 0L);
    }

    @Test
    void errorInsertingWithoutDescription() {
        final var estado = new District();
        estado.abbreviation = "NE";
        assertThrows(ConstraintViolationException.class, () -> repository.save(estado));
    }


    @Test
    void deleteByIdSuccessful() {
        final long id = insertState();
        repository.deleteById(id);

        // Send the SQL command to the database immediately
        repository.flush();

        assertTrue(repository.findById(id).isEmpty());
    }

    @Test
    void deleteByIdViolatesForeignKey() {
        final long id = 1; // São Paulo

        var exception = assertThrows(DataIntegrityViolationException.class, () -> {
            repository.deleteById(id);
            // Send the SQL command to the database immediately
            repository.flush();
        });

        final var constraintName = "FK_CITY__STATE";
        assertTrue(exception.getMessage().contains(constraintName));
    }

}
