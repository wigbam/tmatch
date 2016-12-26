package com.sokol.tmatch.impl;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

import org.hamcrest.Matcher;

public class MatcherFunction<T, U extends Comparable<U>>
        implements
            Function<T, U>,
            Serializable {

    private static final long serialVersionUID = 1L;

    private final Matcher<? super T> matcher;
    private final U resultOnMatch;
    private final U resultOnMismatch;

    public MatcherFunction(final Matcher<? super T> matcher,
            final U resultOnMatch, final U resultOnMismatch) {
        this.matcher = Objects.requireNonNull(matcher);
        this.resultOnMatch = resultOnMatch;
        this.resultOnMismatch = resultOnMismatch;
    }

    public U apply(final T t) {
        return matcher.matches(t) ? resultOnMatch : resultOnMismatch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matcher, resultOnMatch, resultOnMismatch);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MatcherFunction<?, ?> other = (MatcherFunction<?, ?>) obj;

        return Objects.equals(this.resultOnMatch, other.resultOnMatch)
                && Objects.equals(this.resultOnMismatch, other.resultOnMismatch)
                && Objects.equals(this.matcher, other.matcher);
    }

    @Override
    public String toString() {
        return matcher.toString() + " ? " + resultOnMatch + ":"
                + resultOnMismatch + "]";
    }

}
