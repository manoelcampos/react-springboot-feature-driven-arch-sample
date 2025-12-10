package sample.application.api.feature.customer;

import org.springframework.stereotype.Repository;
import sample.application.api.shared.EntityRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends EntityRepository<Customer> {
    Optional<Customer> findBySocialSecurityNumber(String socialSecurityNumber);

    /// {@return list of clients whose name contains a given partial value}
    /// Name can be, for instance:
    /// - "Manoel%" to search for all clients whose name begins with "Manoel".
    /// - "%Campos%" to search for all clients whose name contains "Campos" anywhere.
    /// - "%Silva" to search for all clients whose name ends with "Silva".
    ///
    /// @param name client name, which can contain a % anywhere, to perform a partial search, such as:
    List<Customer> findByNameLike(String name);

    /// {@return a list of customers from a given city}
    /// @param cityId ID of the city to locate the customers.
    List<Customer> findByCityId(long cityId);
}
