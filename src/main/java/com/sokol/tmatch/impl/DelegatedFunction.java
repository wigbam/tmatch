package com.sokol.tmatch.impl;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

public class DelegatedFunction<T, R> implements Function<T, R>, Serializable {

    private static final long serialVersionUID = 1L;

    private final Function<T, R> delegate;
    private final String toString;

    public DelegatedFunction(final Function<T, R> delegate,
            final String toString) {
        this.delegate = Objects.requireNonNull(delegate);
        this.toString = Objects.requireNonNull(toString);
    }

    @Override
    public R apply(final T t) {
        return delegate.apply(t);
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
        final DelegatedFunction<?, ?> other = (DelegatedFunction<?, ?>) obj;

        return this.delegate.equals(other.delegate)
                && Objects.equals(this.toString, other.toString);
    }

    @Override
    public String toString() {
        return toString;
    }
}
