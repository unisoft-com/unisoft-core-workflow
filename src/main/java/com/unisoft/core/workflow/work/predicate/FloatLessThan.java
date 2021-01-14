package com.unisoft.core.workflow.work.predicate;

import com.unisoft.core.workflow.work.ExecutionReport;

/**
 * {@code Float} strict less than comparison.
 *
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
public class FloatLessThan extends StrictComparePredicate<Float> {
    public FloatLessThan(Object key, Float againstValue) {
        super(key, againstValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean apply(ExecutionReport report) {
        return report.context().getData(this.key, Float.class)
                .map(value -> value < againstValue)
                .orElse(false);
    }
}
