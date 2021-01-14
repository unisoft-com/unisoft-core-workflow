package com.unisoft.core.workflow.work.predicate;

import com.unisoft.core.workflow.work.ExecutionReport;

/**
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
public class FloatGreaterThan extends StrictComparePredicate<Float> {
    public FloatGreaterThan(Object key, Float againstValue) {
        super(key, againstValue);
    }

    @Override
    public boolean apply(ExecutionReport report) {
        return report.context().getData(this.key, Float.class)
                .map(value -> value > againstValue)
                .orElse(false);
    }
}
