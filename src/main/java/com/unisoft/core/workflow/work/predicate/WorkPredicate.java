package com.unisoft.core.workflow.work.predicate;

import com.unisoft.core.workflow.work.ExecutionReport;

/**
 * work predicate contract
 *
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
@FunctionalInterface
public interface WorkPredicate {

    /**
     * Apply the predicate on the given execution report.
     *
     * @param report execution report to evaluate
     * @return true if the predicate applies on the given report, false otherwise
     */
    boolean apply(ExecutionReport report);
}
