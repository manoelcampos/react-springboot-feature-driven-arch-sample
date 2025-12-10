package sample.application.api.shared.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import sample.application.api.config.ConstraintKeys;

import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static sample.application.api.shared.util.StringUtil.readableText;

/**
 * Detects a constraint violation (such as a Foreign Key or Unique Constraint) and
 * returns user-friendly information
 * that can be displayed in a friendly error message.
 *
 * @author Manoel Campos
 */
public final class ConstraintViolation {
    /** Private constructor to prevent instantiating the class. */
    private ConstraintViolation(){ throw new UnsupportedOperationException(); }

    /**
     * Format for Foreign Key (FK) names in the database, using fk_source_table__destination_table,
     * where source_table is the table that has the foreign key and destination_table is the referenced table.
     */
    //language=RegExp
    public static final String FK_FORMAT_REGEX = "fk_(\\w+)__(\\w+)";

    /**
     * Format for Unique Constraint (UC) names in the database.
     * More details at {@link ConstraintKeys}.
     */
    //language=RegExp
    public static final String UC_FORMAT_REGEX = "uc_(\\w+)(__(\\w+))+___";

    /**
     * Looks in an exception's error message for the name of a Foreign Key,
     * which indicates that the FK was violated.
     *
     * @param ex {@link DataIntegrityViolationException} thrown
     * @return an {@link Optional} containing a user-friendly message about the FK violation;
     * or an empty Optional if the exception was due to another issue or the name of a FK was not found.
     * @see #FK_FORMAT_REGEX
     */
    public static Optional<String> findForeignKeyMessage(final DataIntegrityViolationException ex) {
        final var matcherOptional = regexMatch(ex, FK_FORMAT_REGEX);
        return matcherOptional.map(ConstraintViolation::fkViolationMsg);
    }

    public static Optional<String> findUniqueConstraintMessage(final DataIntegrityViolationException ex) {
        final var matcherOptional = regexMatch(ex, UC_FORMAT_REGEX);
        return matcherOptional.map(ConstraintViolation::ucViolationMsg);
    }

    public static @NotNull Optional<MatchResult> regexMatch(final DataIntegrityViolationException ex, final String regex) {
        return getPattern(regex)
                      .matcher(ex.getMessage().toLowerCase())
                      .results()
                      .findFirst();
    }

    public static @NotNull Pattern getPattern(final String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    /**
     * Returns a user-friendly message indicating that a foreign key violation has been detected.
     *
     * @param matcher a {@link MatchResult} object returned after executing a regex to check
     * if the error message contains the name of a foreign key following the {@link #FK_FORMAT_REGEX}
     * @return a user-friendly message for the user
     */
    private static String fkViolationMsg(final MatchResult matcher) {
        final var sourceTable = readableText(matcher.group(1));
        final var targetTable = readableText(matcher.group(2));
        return "It was not possible to delete %s because there is a %s associated with it".formatted(targetTable, sourceTable);
    }

    private static String ucViolationMsg(final MatchResult matcher) {
        final var groups = matcher.groupCount();
        final var sourceTable = readableText(matcher.group(1));
        final var fields = IntStream.range(2, groups)
                                    .mapToObj(matcher::group)
                                    .map(StringUtil::readableText)
                                    .toArray(String[]::new);


        final var msg = fields.length == 1 ? "value" : "values";
        return "There is already a %s with the same %s of %s".formatted(sourceTable, msg, String.join(", ", fields));
    }

}
