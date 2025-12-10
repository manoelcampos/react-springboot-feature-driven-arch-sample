package sample.application.api.shared.util;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Adapter containing utility functions for date manipulation,
 * Internally stores a date passed as a parameter,
 * which can be null. Thus, the method is responsible for performing
 * the necessary checks, avoiding {@link NullPointerException}.
 * @author Manoel Campos
 */
public record DateAdapter(@Nullable LocalDate date) {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String format(final LocalDate fimVigenciaAdicional) {
        return DATE_FORMATTER.format(fimVigenciaAdicional);
    }

    /// Checks if the stored [#date()] is equal to or after the current date.
    public boolean isEqualOrAfterNow() {
        return isEqualOrAfter(LocalDate.now());
    }

    /// Checks if the stored [#date()] is equal to or after another date.
    public boolean isEqualOrAfter(final LocalDate other) {
        final var dayBeforeThis = this.date == null ? LocalDate.MIN : this.date.minusDays(1);
        return dayBeforeThis.isAfter(other);
    }

    /// Checks if the stored [#date()] is later than another date.
    public boolean isAfter(final LocalDate other) {
        final var thisDate = this.date == null ? LocalDate.MIN : this.date;
        return thisDate.isAfter(other);
    }

    /// Checks if the stored [#date()] is equal to or before the current date.
    public boolean isEqualOrBeforeNow() {
        return isEqualOrBefore(LocalDate.now());
    }

    /// Checks if the stored [#date()] is equal to or before another date.
    public boolean isEqualOrBefore(final LocalDate other) {
        final var dayAfterThis = this.date == null ? LocalDate.MAX : this.date.plusDays(1);
        return dayAfterThis.isBefore(other);
    }

    /// Checks if the stored [#date()] is earlier than another date.
    /// If the stored date is valid but a null date is not provided as a parameter,
    /// it indicates that there is no date limit for comparison.
    /// Therefore, the stored date will be considered earlier than the provided date.
    /// This situation is useful, for example, when you want to check if the stored date
    /// has not reached an expiration date.
    ///
    /// If there is no expiration date (indicating that the stored date is valid indefinitely),
    /// then the stored date should be considered earlier than the provided date (i.e., valid).
    /// @param other another date to compare
    public boolean isBefore(@Nullable final LocalDate other) {
        final var thisDate = this.date == null ? LocalDate.MAX : this.date;
        final var otherDate = other == null ? LocalDate.MAX : other;
        return thisDate.isBefore(otherDate);
    }
}
