package sample.application.api.feature.city;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.Preconditions;
import sample.application.api.controller.AbstractControllerTest;
import sample.application.api.shared.util.PathUtil;

/**
 * Tests for the REST API of {@link City} implemented by {@link CityController}.
 * @author Manoel Campos
 */
class CityControllerTest extends AbstractControllerTest {
    private static final String RELATIVE_URL = "/city";
    private static final String BY_ID_URL = PathUtil.concat(RELATIVE_URL, "/{id}");

    @Test
    void findById() {
        final long id = 1;
        final var city = new City(id, "City 1");
        client().get()
                .uri(BY_ID_URL, id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(City.class)
                .isEqualTo(city);
    }

    /**
     * Inserts a new City into the database and returns the inserted City.
     * @param cityToInsert City to be inserted
     * @return new City with the generated id
     */
    private City insert(final City cityToInsert) {
        Preconditions.condition(cityToInsert.getId() == null, "The ID of the City to be inserted must be null");

        return client().post()
                       .uri(RELATIVE_URL)
                       .bodyValue(cityToInsert)
                       .exchange()
                       .expectStatus()
                       .isCreated()
                       .expectBody(City.class)
                       .returnResult()
                       .getResponseBody();
    }

    private void delete(final long id) {
        client().delete()
                .uri(BY_ID_URL, id)
                .exchange()
                .expectStatus()
                .isNoContent();

        client().get()
                .uri(BY_ID_URL, id)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
