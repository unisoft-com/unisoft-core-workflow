package com.unisoft.core.workflow.work.predicate;

import com.unisoft.core.workflow.work.ExecutionReport;

/**
 * {@code Integer} strict greater than comparison
 *
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
public class IntegerGreaterThan extends StrictComparePredicate<Integer> {
    public IntegerGreaterThan(Object key, Integer againstValue) {
        super(key, againstValue);
    }

    @Override
    public boolean apply(ExecutionReport report) {
        return report.context().getData(this.key, Integer.class)
                .map(value -> value > againstValue)
                .orElse(false);
    }
}
