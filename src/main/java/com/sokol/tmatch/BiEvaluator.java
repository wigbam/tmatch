package com.sokol.tmatch;

import java.util.function.BiFunction;

@FunctionalInterface
public interface BiEvaluator {
    <T, U, R> R evaluate(BiFunction<? super T, ? super U, ? extends R> function,
            T arg1, U arg2);
}
