package com.glad.watchnext.domain.util;

import java.util.List;

/**
 * Helper methods for working with {@link String}
 * <p>
 * Created by Gautam Lad
 */
public final class StringHelper {
    private StringHelper() {
        throw new IllegalStateException("No instances!");
    }

    //region Delimiter

    /**
     * See {@link #delimited(String, Object...)}
     */
    public static String delimited(final String delimiter, final List<?> input) {
        return delimited(delimiter, input.toArray(new Object[0]));
    }

    /**
     * @param delimiter The delimiter to use
     * @param input     The array to convert
     *
     * @return Converts the input into a delimited string using the delimiter provided
     */
    public static String delimited(final String delimiter, final Object... input) {
        final StringBuilder sb = new StringBuilder();

        ValueHelper.requireValue(input, "Input cannot be null");

        for (final Object i : input) {
            if (sb.length() > 0) {
                sb.append(delimiter);
            }

            sb.append(i.toString());
        }

        return sb.toString();
    }
    //endregion Delimiter
}
