package com.unisoft.core.workflow.engine;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.Workflow;
import com.unisoft.core.workflow.work.ExecutionReport;

/**
 * base workflow engine contract.
 *
 * @author omar.H.Ajmi
 * @since 29/10/2020
 */
@FunctionalInterface
public interface WorkflowEngine {

    /**
     * run the given {@code work} unit with it's execution {@code context} and return the {@code ExecutionReport}.
     *
     * @param work    work unit
     * @param context execution context
     * @return execution report.
     */
    ExecutionReport run(Workflow work, Context context);
}
