package sample.application.api.model;

import jakarta.persistence.ForeignKey;
import jakarta.persistence.UniqueConstraint;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import sample.application.api.ClassUtils;
import sample.application.api.config.ConstraintKeys;
import sample.application.api.shared.model.BaseModel;
import sample.application.api.shared.util.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static sample.application.api.model.ConstraintNamesTest.ConstraintType.FOREIGN_KEY;
import static sample.application.api.model.ConstraintNamesTest.ConstraintType.UNIQUE_CONSTRAINT;
import static sample.application.api.shared.util.ConstraintViolation.FK_FORMAT_REGEX;
import static sample.application.api.shared.util.ConstraintViolation.UC_FORMAT_REGEX;
import static sample.application.api.shared.util.StringUtil.readableText;


/**
 * Verifica se as Foreign Keys e Unique Constraints definidas nas entidades {@link BaseModel}
 * estão no formato esperado.
 *
 * @author Manoel Campos
 */
class ConstraintNamesTest {
    enum ConstraintType {
        /**
         * @see ConstraintViolation#FK_FORMAT_REGEX
         */
        FOREIGN_KEY("fk_source_table__destination_table", FK_FORMAT_REGEX),

        /**
         * @see ConstraintViolation#UC_FORMAT_REGEX
         * @see ConstraintKeys
         */
        UNIQUE_CONSTRAINT("uc_source_table__field1__field2__field_n___", UC_FORMAT_REGEX);

        /**
         * A more user-friendly description of the format that a constraint name should have,
         * according to the {@link #regex} associated with it.
         * @see ConstraintViolation
         */
        private final String formatDescription;

        /**
         * Regex that identifies the format of a constraint's name.
         */
        private final String regex;

        ConstraintType(final String formatDescription, final String regex) {
            this.formatDescription = formatDescription;
            this.regex = regex;
        }
    }

    /**
     * Check if the names of the FKs are according to the expected format.
     */
    @Test
    void foreignKeyNames() {
        final var classes = ClassUtils.getClassesForPackage("");
        for (var aClass : classes) {
            ClassUtils.getFieldForeignKeyStream(aClass)
                      .map(ForeignKey::name)
                      .forEach(fk -> assertConstraintNameFormat(aClass, FOREIGN_KEY, fk));
        }
    }

    /**
     * Check if the names of the Unique Constraints (UCs) are in the expected format.
     * TODO: Needs to find all classes that implement the BaseModel interface
     */
    @Test @Disabled
    void uniqueConstraintNames() {
        final var classes = ClassUtils.getClassesForPackage(getClass().getPackageName());
        for (var aClass : classes) {
            ClassUtils.getUniqueConstraints(aClass)
                      .map(UniqueConstraint::name)
                      .forEach(uc -> assertConstraintNameFormat(aClass, UNIQUE_CONSTRAINT, uc));
        }
    }

    private static void assertConstraintNameFormat(
            final Class<?> aClass, final ConstraintType constraintType,
            final String constraintName)
    {
        final var pattern = ConstraintViolation.getPattern(constraintType.regex);
        assertTrue(pattern.matcher(constraintName).matches(), errorMsg(aClass, constraintType, constraintName));
    }

    /**
     * {@return an error message indicating that the constraint name is not in the expected format}
     * @param aClass class where the constraint violation occurred.
     * @param constraintType type of constraint (Foreign Key or Unique Constraint)
     * @param constraintName name of the FK or UC
     */
    private static String errorMsg(final Class<?> aClass, final ConstraintType constraintType, final String constraintName) {
        final var msg = "Name of %s %s in class %s is not in the expected format %s";
        return msg.formatted(readableText(constraintType.name()), constraintName, aClass.getName(), constraintType.formatDescription);
    }
}
