package com.unisoft.core.workflow.engine;

import com.unisoft.core.workflow.DefaultWorkflowExecutionManager;

/**
 * builder class for {@link PublisherWorkflowEngine}
 *
 * @author omar.H.Ajmi
 * @since 14/11/2020
 */
public class PublisherWorkflowEngineBuilder {

    private DefaultWorkflowExecutionManager manager;


    public PublisherWorkflowEngine build() {
        return new PublisherWorkflowEngine(this.manager);
    }

    public PublisherWorkflowEngineBuilder manager(DefaultWorkflowExecutionManager listener) {
        this.manager = listener;
        return this;
    }
}
