package sample.application.api.config;

import sample.application.api.shared.controller.AbstractController;
import sample.application.api.shared.controller.RestExceptionHandler;
import sample.application.api.shared.util.ConstraintViolation;

/// Utility class that defines unique constraint (UC) names for Entities in the database.
/// When one of these constraints is violated and an exception is generated,
/// the [RestExceptionHandler] checks
/// if the constraint name was contained in the error message,
/// generating a user-friendly error message for the front-end user.
///
/// The name of each UC must follow the pattern `UC_SOURCE_TABLE_NAME__FIELD1__FIELD2__FIELD_N___`.
/// The names used can be as desired, and these names are formatted
/// to be displayed in the front-end error message.
/// The __ separates the table and each of the fields in the constraint name.
/// And a ___ at the end is used to indicate where the original constraint name ends,
/// because some databases like PostgreSQL add a suffix to the name of these constraints,
/// which should not appear in the messages to the user. This format is defined in [ConstraintViolation#UC_FORMAT_REGEX].
///
/// The constraints are centralized here only to allow documentation of this format for all UCs.
///
/// @see AbstractController#update(long, Object)
/// @see AbstractController#insert(Object)
public final class ConstraintKeys {
    /** Private constructor to avoid instantiating the class. */
    private ConstraintKeys(){/**/}

    private static final String UC_SAMPLE = "uc_table__field___";
    private static final String FK_SAMPLE = "fk_source_table__destination_table";

    public static final String UC_STATE_NAME = "uc_state__name___";
    public static final String UC_STATE_ABBREVIATION = "uc_state__abbreviation___";
    public static final String UC_CITY_NAME = "uc_city__name___";

    public static final String FK_CITY__STATE = "fk_city__state";
    public static final String FK_CUSTOMER__CITY = "fk_customer__city";

    public static final String FK_ITEM__ORDER = "fk_item__order";
    public static final String FK_ORDER_ITEM__PRODUCT = "fk_order_item__product";
}
