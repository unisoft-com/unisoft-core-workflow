package com.unisoft.core.workflow.engine;

import com.unisoft.core.event.EventManager;
import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.DefaultWorkflowExecutionManager;
import com.unisoft.core.workflow.Workflow;
import com.unisoft.core.workflow.event.WorkflowEventContext;
import com.unisoft.core.workflow.event.WorkflowExecutionData;
import com.unisoft.core.workflow.event.WorkflowExecutionEvent;
import com.unisoft.core.workflow.work.ExecutionReport;

/**
 * a workflow engine that publishes {@link WorkflowExecutionEvent}
 * according to {@link Workflow} execution states.
 *
 * @author omar.H.Ajmi
 * @since 14/11/2020
 */
public class PublisherWorkflowEngine extends BaseWorkflowEngine {

    PublisherWorkflowEngine(DefaultWorkflowExecutionManager listener) {
        EventManager.INSTANCE.register(DefaultWorkflowExecutionManager.CONTEXT, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionReport run(Workflow work, Context context) {
        EventManager.INSTANCE.broadcast(DefaultWorkflowExecutionManager.CONTEXT,
                new WorkflowExecutionEvent(WorkflowEventContext.WORKFLOW_STARTED.name(),
                        new WorkflowExecutionData(work, null)));

        final ExecutionReport report = super.run(work, context);
        this.determineAndPublishResult(report, work);
        return report;
    }

    /**
     * publishes workflow execution result according to {@link ExecutionReport#status()}
     *
     * @param report   execution report
     * @param workflow executed workflow
     */
    protected void determineAndPublishResult(ExecutionReport report, Workflow workflow) {
        final WorkflowExecutionData data = new WorkflowExecutionData(workflow, report);
        switch (report.status()) {
            case CANCELED:
                this.publishResult(WorkflowEventContext.WORKFLOW_CANCELED, data);
                break;
            case FAILED:
                this.publishResult(WorkflowEventContext.WORKFLOW_FAILED, data);
                break;
            case PENDING:
                this.publishResult(WorkflowEventContext.WORKFLOW_PENDING, data);
                break;
            case COMPLETED:
                this.publishResult(WorkflowEventContext.WORKFLOW_COMPLETED, data);
                break;
            default:
                break;
        }
    }

    /**
     * publishes workflow execution event
     *
     * @param eventContext event context
     * @param data         event data
     */
    private void publishResult(WorkflowEventContext eventContext, WorkflowExecutionData data) {
        EventManager.INSTANCE.broadcast(DefaultWorkflowExecutionManager.CONTEXT,
                new WorkflowExecutionEvent(eventContext.name(), data));
    }
}
