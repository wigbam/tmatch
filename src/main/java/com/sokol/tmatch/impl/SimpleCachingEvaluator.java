package com.sokol.tmatch.impl;

import java.io.Serializable;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sokol.tmatch.Evaluator;

public class SimpleCachingEvaluator implements Evaluator, Serializable {

    private static final long serialVersionUID = 1L;

    private final static Logger LOG = LoggerFactory
            .getLogger(SimpleCachingEvaluator.class);

    private final Evaluator delegate;
    private final Map<Map.Entry<Function<?, ?>, Object>, Object> resultCache;

    public SimpleCachingEvaluator(final Evaluator delegate) {
        this(delegate, false);
    }

    public SimpleCachingEvaluator(final Evaluator delegate,
            final boolean isConcurrent) {
        this.delegate = Objects.requireNonNull(delegate);
        this.resultCache = isConcurrent
                ? new ConcurrentHashMap<>()
                : new HashMap<>();
    }

    @Override
    public <T, R> R evaluate(final Function<? super T, ? extends R> function,
            final T arg) {
        final Map.Entry<Function<?, ?>, Object> key = new SimpleImmutableEntry<>(
                function, arg);

        if (resultCache.containsKey(key)) {
            @SuppressWarnings("unchecked")
            final R result = (R) resultCache.get(key);
            LOG.debug("Using cached result for [{}] with [{}]: [{}]", function,
                    arg, result);
            return result;
        }

        final R result = delegate.evaluate(function, arg);
        LOG.debug("Calculated result for [{}] with {}] and cached: [{}]",
                function, arg, result);
        resultCache.put(key, result);
        return result;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "CachingEvaluator[" + delegate + "]";
    }
}
