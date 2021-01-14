package com.unisoft.core.workflow.event;

import com.unisoft.core.workflow.Workflow;
import com.unisoft.core.workflow.work.ExecutionReport;

import java.util.Objects;

/**
 * basic {@link WorkflowExecutionEvent} data holder
 *
 * @author omar.H.Ajmi
 * @since 14/11/2020
 */
public class WorkflowExecutionData {
    private final Workflow workflow;
    private final ExecutionReport report;

    public WorkflowExecutionData(Workflow workflow, ExecutionReport report) {
        this.workflow = Objects.requireNonNull(workflow, "'workflow' cannot be null");
        this.report = report;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public ExecutionReport getReport() {
        return report;
    }
}
