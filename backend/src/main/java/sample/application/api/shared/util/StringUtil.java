package sample.application.api.shared.util;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.text.WordUtils;

import java.util.Objects;

/**
 * Class with String utility functions.
 * @author Manoel Campos
 */
public final class StringUtil {
    /** Private constructor to prevent instantiating the class */
    private StringUtil(){ throw new UnsupportedOperationException(); }

    /**
     * Gets a more readable name for a table column, to be displayed in messages to the user.
     * Such a name can be used to format error messages (related to a table column)
     * in a way that is more user-friendly.
     *
     * @param columnName name of a column in the database
     * @return a more readable name for the column
     */
    public static String formatColumnName(String columnName){
        /* If the error is a field that represents a foreign key, use the _id because this way,
        only the name of the related table remains, which represents the attribute in the class for the foreign key. */
        columnName = columnName.replaceAll("_id", "").replaceAll("_", " ");
        return camelCaseToSpace(columnName);
    }

    /**
     * Converts a text (such as the name of a table or database field) into a more human-readable format,
     * to be displayed in messages to the user.
     * Such a name can be used to format error messages (related to that table)
     * in a way that is more appropriate for the user.
     *
     * @param text a text usually using underscores to separate words, to be converted into a more readable text
     * @return the converted text
     */
    public static String readableText(final String text){
        return WordUtils.capitalizeFully(text.replaceAll("_", " "));
    }

    /**
     * Converts a text in the format "variasPalavrasJuntas" to "varias palavras juntas".
     *
     * @param text text to be converted
     * @return the text in the new format
     */
    public static String camelCaseToSpace(final String text) {
        return RegExUtils.replacePattern(text, "(?<!^)([A-Z])", " $1").toLowerCase();
    }

    /**
     * {@return a String containing only numeric characters}
     * @param value value to remove non-numeric characters from
     */
    public static String onlyNumbers(final String value) {
        return Objects.requireNonNullElse(value, "").replaceAll("\\D", "");
    }
}
