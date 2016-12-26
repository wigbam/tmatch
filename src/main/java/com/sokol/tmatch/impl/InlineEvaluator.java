package com.sokol.tmatch.impl;

import java.util.function.Function;

import com.sokol.tmatch.Evaluator;

public enum InlineEvaluator implements Evaluator {

    INSTANCE;

    @Override
    public <T, R> R evaluate(final Function<? super T, ? extends R> function,
            final T arg) {
        return function.apply(arg);
    }

    @Override
    public String toString() {
        return "InlineEvaluator";
    }
}
