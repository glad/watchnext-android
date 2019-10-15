package com.glad.watchnext.domain.util;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.functions.ObjectHelper;

/**
 * Wrapper for {@link ObjectHelper}
 * <p>
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class ValueHelper {
    private ValueHelper() {
        throw new IllegalStateException("No instances!");
    }

    //region Custom

    /**
     * Verifies if the object is not null and contains a positive non-zero value or throws a IllegalArgumentException
     * with the given message.
     *
     * @param <T>    the {@link Number} value type
     * @param object the object to verify
     *
     * @return the object itself
     *
     * @throws IllegalArgumentException if object is null or empty
     */
    public static <T extends Number> T verifyPositiveNonZero(final T object) {
        try {
            requireNonNull(object, "Value cannot be null");

            if ((object instanceof Short && object.shortValue() > 0) ||
                    (object instanceof Integer && object.intValue() > 0) ||
                    (object instanceof Long && object.longValue() > 0L) ||
                    (object instanceof Float && object.floatValue() > 0F) ||
                    (object instanceof Double && object.doubleValue() > 0D)) {
                return object;
            }

            throw new IllegalArgumentException("Value of '" + object + "' must be a positive number");
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Verifies if the object is not null and contains a non-empty value or throws a IllegalArgumentException
     * with the given message.
     *
     * @param <T>     the value type
     * @param object  the object to verify
     * @param message the message to use with the NullPointerException
     *
     * @return the object itself
     *
     * @throws IllegalArgumentException if object is null or empty
     */
    public static <T> T requireValue(final T object, final String message) {
        try {
            requireNonNull(object, "Value cannot be null (" + message + ")");

            if ((object instanceof String && object.toString().isEmpty())
                    || (object instanceof List && ((List) object).isEmpty())) {
                throw new IllegalArgumentException(message);
            }

            return object;
        } catch (final NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Verifies if the object is of the required instance or throws a IllegalArgumentException
     * with the given message.
     *
     * @param <T>    the value type
     * @param object the object to verify
     * @param clazz  the class to compare
     *
     * @return the object itself
     *
     * @throws IllegalArgumentException if object is null or doesn't match
     */
    public static <T> T requireInstance(final T object, Class<?> clazz) {
        try {
            requireNonNull(object, "Value cannot be null (for " + clazz.getName() + ")");

            final String message = "Provided class is not of the desired type"
                    + "\n\tExpected: " + clazz.getName()
                    + "\n\tActual: " + object.getClass();
            if (!clazz.isAssignableFrom(object.getClass())) {
                throw new IllegalArgumentException(message);
            }

            return object;
        } catch (final NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * See {@link #requireNonNull(Object, String)}
     */
    public static <T> T requireNonNull(final T object) {
        return ObjectHelper.requireNonNull(object, "Object cannot be null");
    }
    //endregion Custom

    //region Sanitization

    /**
     * Given an input value, if null or missing, returns the provided default
     *
     * @param input        The input value to test
     * @param defaultValue The value to return if input value is null or empty
     * @param <T>          The input value type
     *
     * @return Input value if valid or default value if null
     */
    public static <T> T nullToDefault(final T input, final T defaultValue) {
        try {
            return requireNonNull(input, "Error");
        } catch (final NullPointerException | IllegalArgumentException e) {
            return defaultValue;
        }
    }

    /**
     * @return Original value or null if empty
     */
    public static String emptyToNull(final String input) {
        return input != null && input.trim().isEmpty() ? null : input;
    }

    /**
     * @return Original list or null if empty list
     */
    public static <T> List<T> emptyToNull(final List<T> input) {
        return input != null && input.isEmpty() ? null : input;
    }

    /**
     * @return Original value or empty if null
     */
    public static String nullToEmpty(final String input) {
        return input == null ? "" : input;
    }

    /**
     * @return Original list or empty list if null
     */
    public static <T> List<T> nullToEmpty(final List<T> input) {
        return input == null ? new ArrayList<>() : input;
    }

    /**
     * @return Original number or zero if null
     */
    public static short nullToZero(final Short input) {
        return input == null ? 0 : input;
    }

    /**
     * @return Original number or zero if null
     */
    public static int nullToZero(final Integer input) {
        return input == null ? 0 : input;
    }

    /**
     * @return Original number or zero if null
     */
    public static long nullToZero(final Long input) {
        return input == null ? 0L : input;
    }

    /**
     * @return Original number or zero if null
     */
    public static float nullToZero(final Float input) {
        return input == null ? 0F : input;
    }

    /**
     * @return Original number or zero if null
     */
    public static double nullToZero(final Double input) {
        return input == null ? 0D : input;
    }
    //endregion Sanitization

    //region ObjectHelper
    public static <T> T requireNonNull(final T object, final String message) {
        return ObjectHelper.requireNonNull(object, message);
    }

    public static boolean equals(final Object o1, final Object o2) { // NOPMD
        return ObjectHelper.equals(o1, o1);
    }

    public static int hashCode(final Object o) {
        return ObjectHelper.hashCode(o);
    }

    public static int compare(final int v1, final int v2) {
        return v1 < v2 ? -1 : (v1 > v2 ? 1 : 0);
    }

    public static int compare(final long v1, final long v2) {
        return ObjectHelper.compare(v1, v2);
    }

    static final BiPredicate<Object, Object> EQUALS = new ValueHelper.BiObjectPredicate();

    @SuppressWarnings ("unchecked")
    public static <T> BiPredicate<T, T> equalsPredicate() {
        return ObjectHelper.equalsPredicate();
    }

    public static int verifyPositive(final int value, final String paramName) {
        return ObjectHelper.verifyPositive(value, paramName);
    }

    public static long verifyPositive(final long value, final String paramName) {
        return ObjectHelper.verifyPositive(value, paramName);
    }

    static final class BiObjectPredicate implements BiPredicate<Object, Object> {
        @Override
        public boolean test(final Object o1, final Object o2) {
            return ValueHelper.equals(o1, o2);
        }
    }
    //endregion ObjectHelper
}