package com.sokol.tmatch;

import java.util.Collection;
import java.util.function.Function;

public interface Trait<T, U extends Comparable<U>> {
    Collection<? extends Function<? super T, ? extends U>> getAspects();
}
