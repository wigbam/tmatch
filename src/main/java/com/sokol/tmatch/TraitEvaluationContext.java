package com.sokol.tmatch;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface TraitEvaluationContext<T> extends Evaluator, BiEvaluator {
    T getObject();

    <U, R> R evaluate(Function<? super U, ? extends R> f, U arg);

    <U, V, R> R evaluate(BiFunction<? super U, ? super V, ? extends R> f,
            U arg1, V arg2);
}
