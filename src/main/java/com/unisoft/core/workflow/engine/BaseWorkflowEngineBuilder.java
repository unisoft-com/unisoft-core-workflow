package com.unisoft.core.workflow.engine;

/**
 * builder for {@link BaseWorkflowEngine}
 *
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
public class BaseWorkflowEngineBuilder {

    private BaseWorkflowEngineBuilder() {
        // no-op
    }

    public static WorkflowEngine build() {
        return new BaseWorkflowEngine();
    }
}
