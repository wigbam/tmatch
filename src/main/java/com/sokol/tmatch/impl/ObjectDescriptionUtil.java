package com.sokol.tmatch.impl;

final class ObjectDescriptionUtil {

    private ObjectDescriptionUtil() {}

    public static String describe(final Object o) {
        if (o instanceof Enum<?>) {
            final Enum<?> en = (Enum<?>) o;
            final Enum<?>[] allEnumConstants = en.getClass().getEnumConstants();
            if (allEnumConstants.length < 2) {
                return o.getClass().getSimpleName();
            }
            return o.getClass().getSimpleName() + "[" + en + "]";
        }
        return String.valueOf(o);
    }
}
