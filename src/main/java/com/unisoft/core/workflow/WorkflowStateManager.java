package com.unisoft.core.workflow;

import com.unisoft.core.event.EventListener;
import com.unisoft.core.workflow.work.ExecutionReport;

/**
 * event based contract for managing workflow state
 *
 * @author omar.H.Ajmi
 * @since 14/11/2020
 */
public interface WorkflowStateManager extends EventListener {

    /**
     * manage workflow fail state
     *
     * @param report execution report
     * @param work   workflow
     */
    void onFail(ExecutionReport report, Workflow work);

    /**
     * manage workflow complete state
     *
     * @param report   execution report
     * @param workflow workflow to handle
     */
    void onComplete(ExecutionReport report, Workflow workflow);

    /**
     * manage workflow cancel state
     *
     * @param report   execution report
     * @param workflow workflow to handle
     */
    void onCancel(ExecutionReport report, Workflow workflow);

    /**
     * manage workflow pending state
     *
     * @param report   execution report
     * @param workflow workflow to handle
     */
    void onPending(ExecutionReport report, Workflow workflow);

    /**
     * manage workflow start state
     *
     * @param report   execution report
     * @param workflow workflow to handle
     */
    void onStart(ExecutionReport report, Workflow workflow);
}
