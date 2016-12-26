package com.sokol.tmatch.impl;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.sokol.tmatch.BiEvaluator;
import com.sokol.tmatch.Evaluator;
import com.sokol.tmatch.TraitEvaluationContext;

public final class ImmutableTraitEvaluationContext<T>
        implements
            TraitEvaluationContext<T>,
            Serializable {

    private static final long serialVersionUID = 1L;

    private final T object;
    private final Evaluator evaluator;
    private final BiEvaluator biEvaluator;

    public ImmutableTraitEvaluationContext(final T object,
            final Evaluator evaluator, final BiEvaluator biEvaluator) {
        this.evaluator = Objects.requireNonNull(evaluator);
        this.biEvaluator = Objects.requireNonNull(biEvaluator);
        this.object = object;
    }

    @Override
    public T getObject() {
        return object;
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, evaluator, biEvaluator);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ImmutableTraitEvaluationContext<?> other = (ImmutableTraitEvaluationContext<?>) obj;

        return Objects.equals(this.object, other.object)
                && Objects.equals(this.evaluator, other.evaluator)
                && Objects.equals(this.biEvaluator, other.biEvaluator);
    }

    @Override
    public String toString() {
        return "EvaluationContext[" + object + ", " + evaluator + ", "
                + biEvaluator + "]";
    }

    @Override
    public <U, R> R evaluate(final Function<? super U, ? extends R> f,
            final U arg) {
        return evaluator.evaluate(f, arg);
    }

    @Override
    public <U, V, R> R evaluate(
            final BiFunction<? super U, ? super V, ? extends R> f, final U arg1,
            final V arg2) {
        return biEvaluator.evaluate(f, arg1, arg2);
    }

}
