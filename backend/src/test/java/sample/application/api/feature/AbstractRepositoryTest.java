package sample.application.api.feature;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import sample.application.api.shared.util.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.assertTrue;

/// Base class for implementing integration tests for [org.springframework.stereotype.Repository].
/// **WARNING**: Subclasses should not be final. Including final will generate a warning.
/// @author Manoel Campos
@DataJpaTest
public abstract class AbstractRepositoryTest {
    /**
     * Checks if an exception thrown for a database operation
     * violated a specific constraint.
     * @param ex the generated exception
     * @param constraintName the name of the constraint that is expected to have been violated (such as a UC or FK)
     */
    protected static void assertConstraintViolation(final DataIntegrityViolationException ex, final String constraintName) {
        final var msg = "The name of the constraint expected to be violated was not identified: " + ex.getMessage();
        assertTrue(ConstraintViolation.regexMatch(ex, constraintName).isPresent(), msg);
    }
}
