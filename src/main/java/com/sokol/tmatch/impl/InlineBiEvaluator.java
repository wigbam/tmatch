package com.sokol.tmatch.impl;

import java.util.function.BiFunction;

import com.sokol.tmatch.BiEvaluator;

public enum InlineBiEvaluator implements BiEvaluator {

    INSTANCE;

    @Override
    public <T, U, R> R evaluate(
            final BiFunction<? super T, ? super U, ? extends R> function,
            final T arg1, final U arg2) {
        return function.apply(arg1, arg2);
    }

    @Override
    public String toString() {
        return "InlineBiEvaluator";
    }

}
