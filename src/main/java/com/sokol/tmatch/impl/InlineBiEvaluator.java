package com.sokol.tmatch.impl;

import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sokol.tmatch.BiEvaluator;

public enum InlineBiEvaluator implements BiEvaluator {

    INSTANCE;

    private final static Logger LOG = LoggerFactory
            .getLogger(InlineBiEvaluator.class);

    @Override
    public <T, U, R> R evaluate(
            final BiFunction<? super T, ? super U, ? extends R> function,
            final T arg1, final U arg2) {
        if (LOG.isDebugEnabled()) {
            LOG.trace("Evaluating [{}] with [{}, {}]", function, arg1, arg2);
            final R result = function.apply(arg1, arg2);
            LOG.debug("Evaluated [{}] with [{}, {}]: {}", function, arg1, arg2,
                    result);
            return result;
        } else {
            return function.apply(arg1, arg2);
        }
    }

    @Override
    public String toString() {
        return "InlineBiEvaluator";
    }

}
