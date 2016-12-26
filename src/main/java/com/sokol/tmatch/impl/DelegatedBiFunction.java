package com.sokol.tmatch.impl;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

public class DelegatedBiFunction<T, U, R>
        implements
            BiFunction<T, U, R>,
            Serializable {

    private static final long serialVersionUID = 1L;

    private final BiFunction<T, U, R> delegate;
    private final String toString;

    public DelegatedBiFunction(final BiFunction<T, U, R> delegate,
            final String toString) {
        this.delegate = Objects.requireNonNull(delegate);
        this.toString = Objects.requireNonNull(toString);
    }

    @Override
    public R apply(final T t, final U u) {
        return delegate.apply(t, u);
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
        final DelegatedBiFunction<?, ?, ?> other = (DelegatedBiFunction<?, ?, ?>) obj;

        return this.delegate.equals(other.delegate)
                && Objects.equals(this.toString, other.toString);
    }

    @Override
    public String toString() {
        return toString;
    }
}
