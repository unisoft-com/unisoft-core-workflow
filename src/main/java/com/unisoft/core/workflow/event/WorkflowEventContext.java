package com.unisoft.core.workflow.event;

/**
 * @author omar.H.Ajmi
 * @since 14/11/2020
 */
public enum WorkflowEventContext {
    /**
     * workflow started
     */
    WORKFLOW_STARTED,

    /**
     * workflow completed
     */
    WORKFLOW_COMPLETED,

    /**
     * workflow canceled
     */
    WORKFLOW_CANCELED,

    /**
     * workflow failed
     */
    WORKFLOW_FAILED,

    /**
     * workflow pending
     */
    WORKFLOW_PENDING
}
