package sample.application.api.feature.city;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {
    @Mock
    private CityRepository repository;

    @InjectMocks
    private CityService service;

    @Test
    void insert() {
        final var city = new City("New City");
        final var savedCity = new City(1, "New City");

        Mockito.when(repository.saveAndFlush(city)).thenReturn(savedCity);

        final var fetchedCity = service.save(city);
        assertNotNull(fetchedCity);
        assertEquals(savedCity, fetchedCity);
    }
}
