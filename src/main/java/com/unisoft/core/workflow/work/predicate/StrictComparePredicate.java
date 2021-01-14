package com.unisoft.core.workflow.work.predicate;

import java.util.Objects;

/**
 * strict objects comparison predicate
 *
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
public abstract class StrictComparePredicate<T> implements WorkPredicate {

    protected final Object key;
    protected final T againstValue;

    protected StrictComparePredicate(Object key, T againstValue) {
        this.key = Objects.requireNonNull(key, "'key' cannot be null");
        this.againstValue = Objects.requireNonNull(againstValue, "'againstValue' cannot be null");
    }
}
