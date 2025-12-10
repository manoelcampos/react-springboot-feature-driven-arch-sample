package sample.application.api.feature.product;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * To run tests that use Mockito mocks,
 * it is necessary to use the @ExtendWith annotation
 * so that the mocks are created correctly.
 * @author Manoel Campos
 */
@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT) // used to ignore when a mock method is not called as expected
public abstract class AbstractServiceTest {
}
