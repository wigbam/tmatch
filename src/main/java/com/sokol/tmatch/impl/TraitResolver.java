package com.sokol.tmatch.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;

import com.sokol.tmatch.BiEvaluator;
import com.sokol.tmatch.Evaluator;
import com.sokol.tmatch.Trait;
import com.sokol.tmatch.TraitEvaluationContext;

public class TraitResolver<T, U extends Comparable<U>, R>
        implements
            BiFunction<T, Collection<? extends Trait<T, U>>, R>,
            Serializable {

    private static final long serialVersionUID = 1L;

    private final BiFunction<? super T, ? super Collection<? extends Trait<T, U>>, ? extends Evaluator> evaluatorFactory;
    private final BiFunction<? super T, ? super Collection<? extends Trait<T, U>>, ? extends BiEvaluator> biEvaluatorFactory;
    private final BiFunction<? super TraitEvaluationContext<T>, ? super Collection<? extends Trait<T, U>>, ? extends R> traitsEvaluator;

    public TraitResolver(
            final BiFunction<? super T, ? super Collection<? extends Trait<T, U>>, ? extends Evaluator> evaluatorFactory,
            final BiFunction<? super T, ? super Collection<? extends Trait<T, U>>, ? extends BiEvaluator> biEvaluatorFactory,
            final BiFunction<? super TraitEvaluationContext<T>, ? super Collection<? extends Trait<T, U>>, ? extends R> traitsEvaluator) {
        this.evaluatorFactory = Objects.requireNonNull(evaluatorFactory);
        this.biEvaluatorFactory = Objects.requireNonNull(biEvaluatorFactory);
        this.traitsEvaluator = Objects.requireNonNull(traitsEvaluator);
    }

    @Override
    public R apply(final T object,
            final Collection<? extends Trait<T, U>> traits) {
        final Evaluator evaluator = evaluatorFactory.apply(object, traits);
        final BiEvaluator biEvaluator = biEvaluatorFactory.apply(object,
                traits);
        return this.traitsEvaluator.apply(new ImmutableTraitEvaluationContext<>(
                object, evaluator, biEvaluator), traits);
    }

    public static <T, U extends Comparable<U>, R> TraitResolver<T, U, R> withInlineEvaluators(
            final BiFunction<? super TraitEvaluationContext<T>, ? super Collection<? extends Trait<T, U>>, ? extends R> traitsEvaluator) {
        return new TraitResolver<>(getInlineEvaluatorFactory(),
                getInlineBiEvaluatorFactory(), traitsEvaluator);
    }

    public static <T, U extends Comparable<U>, R> TraitResolver<T, U, R> withInlineEvaluator(
            final BiFunction<? super TraitEvaluationContext<T>, ? super Collection<? extends Trait<T, U>>, ? extends R> traitsEvaluator,
            final BiFunction<? super T, ? super Collection<? extends Trait<T, U>>, ? extends BiEvaluator> biEvaluatorFactory) {
        return new TraitResolver<>(getInlineEvaluatorFactory(),
                biEvaluatorFactory, traitsEvaluator);
    }

    public static <T, U extends Comparable<U>, R> TraitResolver<T, U, R> withInlineBiEvaluator(
            final BiFunction<? super TraitEvaluationContext<T>, ? super Collection<? extends Trait<T, U>>, ? extends R> traitsEvaluator,
            final BiFunction<? super T, ? super Collection<? extends Trait<T, U>>, ? extends Evaluator> evaluatorFactory) {
        return new TraitResolver<>(evaluatorFactory,
                getInlineBiEvaluatorFactory(), traitsEvaluator);
    }

    private static <T, U extends Comparable<U>> BiFunction<? super T, ? super Collection<? extends Trait<T, U>>, ? extends Evaluator> getInlineEvaluatorFactory() {
        return (x, y) -> InlineEvaluator.INSTANCE;
    }

    private static <T, U extends Comparable<U>> BiFunction<? super T, ? super Collection<? extends Trait<T, U>>, ? extends BiEvaluator> getInlineBiEvaluatorFactory() {
        return (x, y) -> InlineBiEvaluator.INSTANCE;
    }
}
