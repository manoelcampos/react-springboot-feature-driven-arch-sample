package sample.application.api.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/// Base class for implementing integration tests for [org.springframework.stereotype.Repository]
/// WARNING: Subclasses should not be final. Making them final will generate a warning.
///
/// [AutoConfigureTestDatabase] is used to NOT replace the database created by the application
/// with the in-memory embedded DB that [DataJpaTest] creates.
/// This way, the same application database will be used for the tests (with the same configurations).
///
/// @author Manoel Campos
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class AbstractRepositoryTest {

}
