package com.sokol.tmatch.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import com.sokol.tmatch.Trait;

public class ImmutableTrait<T, U extends Comparable<U>> implements Trait<T, U> {

    private final Collection<Function<? super T, ? extends U>> aspectView;
    private final String name;

    public ImmutableTrait(
            final Collection<Function<? super T, ? extends U>> aspects,
            final String name) {
        this.aspectView = Collections
                .unmodifiableCollection(new ArrayList<>(aspects));
        this.name = name;
    }

    @Override
    public Collection<? extends Function<? super T, ? extends U>> getAspects() {
        return aspectView;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ImmutableTrait[" + (name == null ? aspectView : name) + "]";
    }
}
