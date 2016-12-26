package com.sokol.tmatch;

import java.util.function.Function;

@FunctionalInterface
public interface Evaluator {
    <U, R> R evaluate(Function<? super U, ? extends R> function, U arg);
}
