package com.unisoft.core.workflow.work.predicate;

import com.unisoft.core.workflow.work.ExecutionReport;

/**
 * integer strict less than comparison
 *
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
public class IntegerLessThan extends StrictComparePredicate<Integer> {
    public IntegerLessThan(Object key, Integer againstValue) {
        super(key, againstValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean apply(ExecutionReport report) {
        return report.context().getData(this.key, Integer.class)
                .map(value -> value < againstValue)
                .orElse(false);
    }
}
