package com.sokol.tmatch.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sokol.tmatch.BiEvaluator;

public class SimpleCachingBiEvaluator implements BiEvaluator, Serializable {

    private static final long serialVersionUID = 1L;

    private final static Logger LOG = LoggerFactory
            .getLogger(SimpleCachingBiEvaluator.class);

    private final BiEvaluator delegate;
    private final Map<Object, Object> resultCache;

    public SimpleCachingBiEvaluator(final BiEvaluator delegate) {
        this(delegate, false);
    }

    public SimpleCachingBiEvaluator(final BiEvaluator delegate,
            final boolean isConcurrent) {
        this.delegate = Objects.requireNonNull(delegate);
        this.resultCache = isConcurrent
                ? new ConcurrentHashMap<>()
                : new HashMap<>();
    }

    @Override
    public <T, U, R> R evaluate(
            final BiFunction<? super T, ? super U, ? extends R> function,
            final T arg1, final U arg2) {
        final Object key = Arrays.asList(function, arg1, arg2);

        if (resultCache.containsKey(key)) {
            @SuppressWarnings("unchecked")
            final R result = (R) resultCache.get(key);
            LOG.debug("Using cached result for [{}] with [{}, {}]: [{}]",
                    function, arg1, arg2, result);
            return result;
        }

        final R result = delegate.evaluate(function, arg1, arg2);
        LOG.debug("Cached evaluation result of [{}] with [{}, {}]: [{}]",
                function, arg1, arg2, result);
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
