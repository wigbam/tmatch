package com.sokol.tmatch.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiFunction;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.sokol.tmatch.Trait;
import com.sokol.tmatch.TraitEvaluationContext;

public class TraitResolverTest {

    private final Trait<String, Double> testTrait1 = new ImmutableTrait<>(
            Arrays.asList(
                    new MatcherFunction<>(Matchers.startsWith("blah"), 0.5,
                            Double.NaN),
                    new MatcherFunction<>(Matchers.containsString("ah"), 0.5,
                            Double.NaN),
                    new MatcherFunction<>(Matchers.containsString("foo"), 0.5,
                            Double.NaN)),
            "TestTrait1");

    private final Trait<String, Double> testTrait2 = new ImmutableTrait<>(
            Arrays.asList(
                    new MatcherFunction<>(Matchers.startsWith("blah"), 0.5,
                            Double.NaN),
                    new MatcherFunction<>(Matchers.containsString("ah"), 0.5,
                            Double.NaN),
                    new MatcherFunction<>(Matchers.endsWith("foo"), 0.5,
                            Double.NaN)),
            "TestTrait2");

    private final Trait<String, Double> testTrait3 = new ImmutableTrait<>(
            Arrays.asList(new MatcherFunction<>(Matchers.equalTo("blah foo"),
                    2d, Double.NaN)),
            "TestTrait3");

    @Test
    public void testSingleMismatch() {
        final BiFunction<TraitEvaluationContext<String>, Trait<String, Double>, Double> sumEvaluator = TraitResolverUtil
                .getSummarizingDoubleTraitEvaluator();
        final BiFunction<TraitEvaluationContext<String>, Collection<? extends Trait<String, Double>>, Collection<Trait<String, Double>>> f1 = TraitResolverUtil
                .filteringTraits(TraitResolverUtil.removeNans(sumEvaluator));
        final BiFunction<TraitEvaluationContext<String>, Collection<? extends Trait<String, Double>>, Collection<Trait<String, Double>>> f2 = TraitResolverUtil
                .filteringTraits(TraitResolverUtil.leaveOnlyMaxScoringElements(
                        sumEvaluator, Comparator.naturalOrder()));

        final TraitResolver<String, Double, Collection<Trait<String, Double>>> underTest = new TraitResolver<>(
                TraitResolverUtil.getSimpleCachingEvaluatorFactory(),
                TraitResolverUtil.getSimpleCachingBiEvaluatorFactory(),
                TraitResolverUtil.chain(f1, f2));

        Assert.assertTrue(
                underTest.apply("blah", Arrays.asList(testTrait1)).isEmpty());
    }

    @Test
    public void testSingleMatch() {
        final BiFunction<TraitEvaluationContext<String>, Trait<String, Double>, Double> sumEvaluator = TraitResolverUtil
                .getSummarizingDoubleTraitEvaluator();
        final BiFunction<TraitEvaluationContext<String>, Collection<? extends Trait<String, Double>>, Collection<Trait<String, Double>>> f1 = TraitResolverUtil
                .filteringTraits(TraitResolverUtil.removeNans(sumEvaluator));
        final BiFunction<TraitEvaluationContext<String>, Collection<? extends Trait<String, Double>>, Collection<Trait<String, Double>>> f2 = TraitResolverUtil
                .filteringTraits(TraitResolverUtil.leaveOnlyMaxScoringElements(
                        sumEvaluator, Comparator.naturalOrder()));

        final TraitResolver<String, Double, Collection<Trait<String, Double>>> underTest = new TraitResolver<>(
                TraitResolverUtil.getSimpleCachingEvaluatorFactory(),
                TraitResolverUtil.getSimpleCachingBiEvaluatorFactory(),
                TraitResolverUtil.chain(f1, f2));

        Assert.assertEquals(Arrays.asList(testTrait1),
                underTest.apply("blah foo", Arrays.asList(testTrait1)));
    }

    @Test
    public void testMultipleMatches() {
        final BiFunction<TraitEvaluationContext<String>, Trait<String, Double>, Double> sumEvaluator = TraitResolverUtil
                .getSummarizingDoubleTraitEvaluator();
        final BiFunction<TraitEvaluationContext<String>, Collection<? extends Trait<String, Double>>, Collection<Trait<String, Double>>> f1 = TraitResolverUtil
                .filteringTraits(TraitResolverUtil.removeNans(sumEvaluator));
        final BiFunction<TraitEvaluationContext<String>, Collection<? extends Trait<String, Double>>, Collection<Trait<String, Double>>> f2 = TraitResolverUtil
                .filteringTraits(TraitResolverUtil.leaveOnlyMaxScoringElements(
                        sumEvaluator, Comparator.naturalOrder()));

        final TraitResolver<String, Double, Collection<Trait<String, Double>>> underTest = new TraitResolver<>(
                TraitResolverUtil.getSimpleCachingEvaluatorFactory(),
                TraitResolverUtil.getSimpleCachingBiEvaluatorFactory(),
                TraitResolverUtil.chain(f1, f2));

        Assert.assertEquals(Arrays.asList(testTrait1, testTrait2), underTest
                .apply("blah foo", Arrays.asList(testTrait1, testTrait2)));
    }

    @Test
    public void testMultipleMatches_SelectsHighestScore() {
        final BiFunction<TraitEvaluationContext<String>, Trait<String, Double>, Double> sumEvaluator = TraitResolverUtil
                .getSummarizingDoubleTraitEvaluator();
        final BiFunction<TraitEvaluationContext<String>, Collection<? extends Trait<String, Double>>, Collection<Trait<String, Double>>> f1 = TraitResolverUtil
                .filteringTraits(TraitResolverUtil.removeNans(sumEvaluator));
        final BiFunction<TraitEvaluationContext<String>, Collection<? extends Trait<String, Double>>, Collection<Trait<String, Double>>> f2 = TraitResolverUtil
                .filteringTraits(TraitResolverUtil.leaveOnlyMaxScoringElements(
                        sumEvaluator, Comparator.naturalOrder()));

        final TraitResolver<String, Double, Collection<Trait<String, Double>>> underTest = new TraitResolver<>(
                TraitResolverUtil.getSimpleCachingEvaluatorFactory(),
                TraitResolverUtil.getSimpleCachingBiEvaluatorFactory(),
                TraitResolverUtil.chain(f1, f2));

        Assert.assertEquals(Arrays.asList(testTrait3), underTest.apply(
                "blah foo", Arrays.asList(testTrait1, testTrait2, testTrait3)));
    }
}
