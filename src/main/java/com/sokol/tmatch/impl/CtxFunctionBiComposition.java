package com.sokol.tmatch.impl;

import java.util.Objects;
import java.util.function.BiFunction;

import com.sokol.tmatch.TraitEvaluationContext;

public class CtxFunctionBiComposition<T, U, V, R>
        implements
            BiFunction<TraitEvaluationContext<T>, U, R> {

    private final BiFunction<TraitEvaluationContext<T>, ? super U, ? extends V> a;
    private final BiFunction<TraitEvaluationContext<T>, ? super V, ? extends R> b;

    public CtxFunctionBiComposition(
            final BiFunction<TraitEvaluationContext<T>, ? super U, ? extends V> a,
            final BiFunction<TraitEvaluationContext<T>, ? super V, ? extends R> b) {
        this.a = Objects.requireNonNull(a);
        this.b = Objects.requireNonNull(b);
    }

    @Override
    public R apply(final TraitEvaluationContext<T> ctx, final U arg) {
        return ctx.evaluate(b, ctx, ctx.evaluate(a, ctx, arg));
    }

    @Override
    public String toString() {
        return b + "(" + a + ")";
    }

    public static <T, U, V, R> CtxFunctionBiComposition<T, U, V, R> of(
            final BiFunction<TraitEvaluationContext<T>, ? super U, ? extends V> a,
            final BiFunction<TraitEvaluationContext<T>, ? super V, ? extends R> b) {
        return new CtxFunctionBiComposition<>(a, b);
    }
}
