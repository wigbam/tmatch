package com.sokol.tmatch.impl;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;

public class DelegatedPredicate<T> implements Predicate<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final Predicate<T> delegate;
    private final String toString;

    public DelegatedPredicate(final Predicate<T> delegate,
            final String toString) {
        this.delegate = Objects.requireNonNull(delegate);
        this.toString = Objects.requireNonNull(toString);
    }

    @Override
    public boolean test(final T t) {
        return delegate.test(t);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DelegatedPredicate<?> other = (DelegatedPredicate<?>) obj;

        return this.delegate.equals(other.delegate)
                && Objects.equals(this.toString, other.toString);
    }

    @Override
    public String toString() {
        return toString;
    }

    public static <T> DelegatedPredicate<T> of(final Predicate<T> delegate,
            final String toString) {
        return new DelegatedPredicate<>(delegate, toString);
    }
}
