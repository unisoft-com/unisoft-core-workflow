package com.unisoft.core.workflow.work.predicate;

import com.unisoft.core.workflow.work.ExecutionReport;

import java.math.BigDecimal;

/**
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
public class BigDecimalGreaterThan extends StrictComparePredicate<BigDecimal> {
    public BigDecimalGreaterThan(Object key, BigDecimal againstValue) {
        super(key, againstValue);
    }

    @Override
    public boolean apply(ExecutionReport report) {
        return report.context().getData(this.key, BigDecimal.class)
                .map(value -> value.compareTo(againstValue) > 0)
                .orElse(false);
    }
}
