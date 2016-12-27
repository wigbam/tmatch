package com.sokol.tmatch.impl;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sokol.tmatch.Evaluator;

public enum InlineEvaluator implements Evaluator {

    INSTANCE;

    private final static Logger LOG = LoggerFactory
            .getLogger(InlineEvaluator.class);

    @Override
    public <T, R> R evaluate(final Function<? super T, ? extends R> function,
            final T arg) {
        if (LOG.isDebugEnabled()) {
            LOG.trace("Evaluating [{}] with [{}]", function, arg);
            final R result = function.apply(arg);
            LOG.debug("Evaluated [{}] with [{}]: {}", function, arg, result);
            return result;
        } else {
            return function.apply(arg);
        }
    }

    @Override
    public String toString() {
        return "InlineEvaluator";
    }
}
