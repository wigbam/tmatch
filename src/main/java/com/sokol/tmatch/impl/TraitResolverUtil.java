package com.sokol.tmatch.impl;

import static com.sokol.tmatch.impl.ObjectDescriptionUtil.describe;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sokol.tmatch.BiEvaluator;
import com.sokol.tmatch.Evaluator;
import com.sokol.tmatch.Trait;
import com.sokol.tmatch.TraitEvaluationContext;

public final class TraitResolverUtil {

    private TraitResolverUtil() {}

    private static final Predicate<Number> IS_NUMBER_NOT_NAN = DelegatedPredicate
            .of(x -> !Double.isNaN(x.doubleValue()), "isNotNaN");

    private static final Predicate<Optional<? extends Number>> IS_OPTIONAL_NUMBER_NOT_NAN = DelegatedPredicate
            .of(x -> x.isPresent() && !Double.isNaN(x.get().doubleValue()),
                    "isNotNaN");

    private static final BiFunction<Object, Object, Evaluator> SIMPLE_CACHING_EVALUATOR_FACTORY = DelegatedBiFunction
            .of((c, t) -> new SimpleCachingEvaluator(InlineEvaluator.INSTANCE),
                    "SimpleCachingEvaluatorFactory");

    private static final BiFunction<Object, Object, BiEvaluator> SIMPLE_CACHING_BIEVALUATOR_FACTORY = DelegatedBiFunction
            .of((c, t) -> new SimpleCachingBiEvaluator(
                    InlineBiEvaluator.INSTANCE),
                    "SimpleCachingBiEvaluatorFactory");

    public static <T, U extends Comparable<U>, R>
            BiFunction<TraitEvaluationContext<T>, Collection<? extends Trait<T, U>>, Predicate<Trait<T, U>>>
            leaveOnlyMaxScoringElements(
                    final BiFunction<? super TraitEvaluationContext<T>, ? super Trait<T, U>, ? extends R> tEval,
                    final Comparator<? super R> cmp) {
        return leaveOnlyMaxScoringElements(tEval, cmp,
                describe(tEval) + "->" + describe(cmp));
    }

    public static <T, U extends Comparable<U>, R>
            BiFunction<TraitEvaluationContext<T>, Collection<? extends Trait<T, U>>, Predicate<Trait<T, U>>>
            leaveOnlyMaxScoringElements(
                    final BiFunction<? super TraitEvaluationContext<T>, ? super Trait<T, U>, ? extends R> tEval,
                    final Comparator<R> cmp, final String toString) {
        return new DelegatedBiFunction<>((c, t) -> {
            final Optional<R> maxScore = t.stream()
                    .map(x -> (R) c.evaluate(tEval, c, x)).max(cmp);
            if (!maxScore.isPresent()) {
                return x -> true;
            } else {
                R trueMaxScoe = maxScore.get();
                return x -> cmp.compare(trueMaxScoe,
                        c.evaluate(tEval, c, x)) == 0;
            }
        }, "leaveMaxScoringElements(" + toString + ")");
    }
    public static <T, U extends Number & Comparable<U>>
            BiFunction<TraitEvaluationContext<T>, Collection<? extends Trait<T, U>>, Predicate<? super Trait<T, U>>>
            removeNans(
                    final BiFunction<? super TraitEvaluationContext<T>, ? super Trait<T, U>, U> traitEvaluator) {
        return filterTraits(traitEvaluator, IS_NUMBER_NOT_NAN);
    }

    public static <T, U extends Number & Comparable<U>>
            BiFunction<TraitEvaluationContext<T>, Collection<? extends Trait<T, U>>, Predicate<? super Trait<T, U>>>
            removeOptionalNans(
                    final BiFunction<? super TraitEvaluationContext<T>, ? super Trait<T, U>, Optional<U>> traitEvaluator) {
        return filterTraits(traitEvaluator, IS_OPTIONAL_NUMBER_NOT_NAN);
    }

    public static <T, U extends Comparable<U>, R>
            BiFunction<TraitEvaluationContext<T>, Collection<? extends Trait<T, U>>, Predicate<? super Trait<T, U>>>
            filterTraits(
                    final BiFunction<? super TraitEvaluationContext<T>, ? super Trait<T, U>, ? extends R> tEval,
                    final Predicate<? super R> predicate) {
        return new DelegatedBiFunction<>(
                (ctx, t) -> x -> predicate.test(ctx.evaluate(tEval, ctx, x)),
                predicate + "(" + tEval + ")");
    }

    public static <T, U extends Comparable<U>>
            BiFunction<TraitEvaluationContext<T>, Collection<? extends Trait<T, U>>, Collection<Trait<T, U>>>
            filteringTraits(
                    final BiFunction<? super TraitEvaluationContext<T>, ? super Collection<? extends Trait<T, U>>, ? extends Predicate<? super Trait<T, U>>> filterResolverFactory) {
        return new DelegatedBiFunction<>(
                (c, t) -> t.stream().filter(filterResolverFactory.apply(c, t))
                        .collect(Collectors.toList()),
                "filter[" + filterResolverFactory + "]->list");
    }

    public static <T, U extends Number & Comparable<U>>
            BiFunction<TraitEvaluationContext<T>, Trait<T, U>, Integer>
            getSummarizingIntTraitEvaluator() {
        return getAggregatingTraitEvaluator(Number::intValue, (x, y) -> x + y,
                0, "sum");
    }

    public static <T, U extends Number & Comparable<U>>
            BiFunction<TraitEvaluationContext<T>, Trait<T, U>, Long>
            getSummarizingLongTraitEvaluator() {
        return getAggregatingTraitEvaluator(Number::longValue, (x, y) -> x + y,
                0L, "sum");
    }

    public static <T, U extends Number & Comparable<U>>
            BiFunction<TraitEvaluationContext<T>, Trait<T, U>, Double>
            getSummarizingDoubleTraitEvaluator() {
        return getAggregatingTraitEvaluator(Number::doubleValue,
                (x, y) -> x + y, 0d, "sum");
    }

    public static <T, U extends Comparable<U>>
            BiFunction<TraitEvaluationContext<T>, Trait<T, U>, Optional<U>>
            getAggregatingTraitEvaluator(final BinaryOperator<U> accumulator) {
        return (c, t) -> t.getAspects().stream()
                .map(a -> (U) c.evaluate(a, c.getObject())).reduce(accumulator);
    }

    public static <T, U extends Comparable<U>, R>
            BiFunction<TraitEvaluationContext<T>, Trait<T, U>, Optional<R>>
            getAggregatingTraitEvaluator(final Function<? super U, R> converter,
                    final BinaryOperator<R> accumulator,
                    final String toString) {
        return new DelegatedBiFunction<>((c, t) -> t.getAspects().stream()
                .map(a -> c.evaluate(a, c.getObject())).map(converter)
                .reduce(accumulator), toString);
    }

    public static <T, U extends Comparable<U>, R>
            BiFunction<TraitEvaluationContext<T>, Trait<T, U>, R>
            getAggregatingTraitEvaluator(final Function<? super U, R> converter,
                    final BinaryOperator<R> accumulator, final R ident,
                    final String toString) {
        return new DelegatedBiFunction<>((c, t) -> t.getAspects().stream()
                .map(a -> c.evaluate(a, c.getObject())).map(converter)
                .reduce(ident, accumulator), toString);
    }

    public static <T, U extends Comparable<U>, R>
            BiFunction<TraitEvaluationContext<T>, Trait<T, U>, R>
            getIdentityAggregatingTraitEvaluator(
                    final Function<? super U, R> converter, final R identity,
                    final BinaryOperator<R> accumulator) {
        return (c, t) -> t.getAspects().stream()
                .map(a -> c.evaluate(a, c.getObject())).map(converter)
                .reduce(identity, accumulator);
    }

    public static <T, U, V, R> BiFunction<TraitEvaluationContext<T>, U, R>
            chain(final BiFunction<TraitEvaluationContext<T>, ? super U, ? extends V> a,
                    final BiFunction<TraitEvaluationContext<T>, ? super V, ? extends R> b) {
        return CtxFunctionBiComposition.of(a, b);
    }

    @SafeVarargs
    public static <T, U extends Comparable<U>, R>
            BiFunction<TraitEvaluationContext<T>, ? super R, ? extends R>
            chain(final BiFunction<TraitEvaluationContext<T>, ? super R, ? extends R>... evaluators) {
        BiFunction<TraitEvaluationContext<T>, ? super R, ? extends R> f = evaluators[0];
        for (int i = 1; i < evaluators.length; i++) {
            f = CtxFunctionBiComposition.of(f, evaluators[i]);
        }
        return f;
    }

    public static BiFunction<Object, Object, Evaluator>
            getSimpleCachingEvaluatorFactory() {
        return SIMPLE_CACHING_EVALUATOR_FACTORY;
    }

    public static BiFunction<Object, Object, BiEvaluator>
            getSimpleCachingBiEvaluatorFactory() {
        return SIMPLE_CACHING_BIEVALUATOR_FACTORY;
    }
}
