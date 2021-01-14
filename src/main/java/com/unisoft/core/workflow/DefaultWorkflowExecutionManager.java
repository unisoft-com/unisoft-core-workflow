package com.unisoft.core.workflow;

import com.google.common.eventbus.Subscribe;
import com.unisoft.core.event.Event;
import com.unisoft.core.util.log.LogUtil;
import com.unisoft.core.workflow.event.WorkflowEventContext;
import com.unisoft.core.workflow.event.WorkflowExecutionData;
import com.unisoft.core.workflow.event.WorkflowExecutionEvent;
import com.unisoft.core.workflow.work.ExecutionReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * default workflow state manager, provides basic logging.
 *
 * @author omar.H.Ajmi
 * @since 14/11/2020
 */
public class DefaultWorkflowExecutionManager implements WorkflowStateManager {
    public static final String CONTEXT = "WORKFLOW";
    private static final Logger log = LoggerFactory.getLogger(DefaultWorkflowExecutionManager.class);
    private static final String LOG_MESSAGE = "managing {} state for workflow {}";

    /**
     * {@inheritDoc}
     */
    @Subscribe
    @Override
    public void handle(Event event) {
        final WorkflowExecutionEvent executionEvent = (WorkflowExecutionEvent) event;
        final WorkflowExecutionData data = executionEvent.data();

        LogUtil.debug(log, "{}: {} - {}", event.identifier(), data.getWorkflow().name(), data.getWorkflow().id());

        if (executionEvent.identifier().equals(WorkflowEventContext.WORKFLOW_STARTED.name())) {
            this.onStart(data.getReport(), data.getWorkflow());
        } else if (executionEvent.identifier().equals(WorkflowEventContext.WORKFLOW_COMPLETED.name())) {
            this.onComplete(data.getReport(), data.getWorkflow());
        } else if (executionEvent.identifier().equals(WorkflowEventContext.WORKFLOW_FAILED.name())) {
            this.onFail(data.getReport(), data.getWorkflow());
        } else if (executionEvent.identifier().equals(WorkflowEventContext.WORKFLOW_PENDING.name())) {
            this.onPending(data.getReport(), data.getWorkflow());
        } else if (executionEvent.identifier().equals(WorkflowEventContext.WORKFLOW_CANCELED.name())) {
            this.onCancel(data.getReport(), data.getWorkflow());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFail(ExecutionReport report, Workflow workflow) {
        LogUtil.debug(log, LOG_MESSAGE, report.status(), workflow.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onComplete(ExecutionReport report, Workflow workflow) {
        LogUtil.debug(log, LOG_MESSAGE, report.status(), workflow.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancel(ExecutionReport report, Workflow workflow) {
        LogUtil.debug(log, LOG_MESSAGE, report.status(), workflow.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPending(ExecutionReport report, Workflow workflow) {
        LogUtil.debug(log, LOG_MESSAGE, report.status(), workflow.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ExecutionReport report, Workflow workflow) {
        LogUtil.debug(log, "managing START state for workflow {}", workflow.name());
    }
}
