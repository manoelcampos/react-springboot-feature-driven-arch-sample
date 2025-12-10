package sample.application.api.shared.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/// A class with utility functions for [List]s.
/// @author Manoel Campos
public final class ListUtil {
    /** Private constructor to prevent instantiating the class */
    private ListUtil() { throw new UnsupportedOperationException(); }

    /// Creates a mutable [LinkedList] from the values passed as parameters
    /// @param items items to be added to the list
    /// @return the new LinkedList
    /// @param <T> type of the elements in the list
    @SafeVarargs
    public static <T> List<T> of(final T ...items){
        final var list = new LinkedList<T>();
        Collections.addAll(list, items);
        return list;
    }

    /// Adds an item to a list and returns the list itself.
    /// @param list the list to add an item to
    /// @param item the item to be added
    /// @return the list passed as a parameter
    /// @param <T> the type of the list elements
    public static <T> List<T> add(final List<T> list, final T item){
        list.add(item);
        return list;
    }
}
