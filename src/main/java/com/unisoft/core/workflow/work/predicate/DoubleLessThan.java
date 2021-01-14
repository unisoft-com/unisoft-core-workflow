package com.unisoft.core.workflow.work.predicate;

import com.unisoft.core.workflow.work.ExecutionReport;

/**
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
public class DoubleLessThan extends StrictComparePredicate<Double> {
    public DoubleLessThan(Object key, Double againstValue) {
        super(key, againstValue);
    }

    @Override
    public boolean apply(ExecutionReport report) {
        return report.context().getData(this.key, Double.class)
                .map(value -> value < againstValue)
                .orElse(false);
    }
}
