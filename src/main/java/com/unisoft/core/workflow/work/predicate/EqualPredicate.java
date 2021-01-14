package com.unisoft.core.workflow.work.predicate;

import com.unisoft.core.workflow.work.ExecutionReport;

/**
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
public class EqualPredicate implements WorkPredicate {
    private final Object key;
    private final Object expectedValue;

    public EqualPredicate(Object key, Object expectedValue) {
        this.key = key;
        this.expectedValue = expectedValue;
    }

    /**
     * applies equality predicate on a value from work data context
     *
     * @param report execution report to evaluate
     * @return true if expected value equals actual value, false if not equal or actual value does not exist
     */
    @Override
    public boolean apply(ExecutionReport report) {
        return report.context().getData(this.key)
                .map(expectedValue::equals)
                .orElse(false);
    }
}
