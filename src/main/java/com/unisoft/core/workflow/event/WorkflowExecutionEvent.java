package com.unisoft.core.workflow.event;

import com.unisoft.core.event.ApplicationEvent;

/**
 * basic workflow execution event
 *
 * @author omar.H.Ajmi
 * @since 14/11/2020
 */
public class WorkflowExecutionEvent extends ApplicationEvent {

    private final WorkflowExecutionData load;

    public WorkflowExecutionEvent(String identifier, WorkflowExecutionData load) {
        super(identifier, load);
        this.load = load;
    }

    @Override
    public WorkflowExecutionData data() {
        return this.load;
    }
}
