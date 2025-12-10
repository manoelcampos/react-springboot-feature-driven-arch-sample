package sample.application.api.feature.district;

import org.junit.jupiter.api.Test;
import sample.application.api.controller.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DistrictControllerTest extends AbstractControllerTest {
    @Test
    void findById() {
        final var expectedState = new District(1);
        expectedState.name = "São Paulo";

        District fetchedDistrict = client().get()
                                           .uri("/district/{id}", 1)
                                           .exchange() // envia a requisição
                                           .expectStatus()
                                           .isOk()
                                           .expectBody(District.class)
                                           .isEqualTo(expectedState)
                                           .returnResult()
                                           .getResponseBody();

        assertNotNull(fetchedDistrict);
        assertEquals(expectedState.name, fetchedDistrict.name);
    }
}
