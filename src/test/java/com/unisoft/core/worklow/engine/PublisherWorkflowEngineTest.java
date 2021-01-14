package com.unisoft.core.worklow.engine;

import com.unisoft.core.workflow.DefaultWorkflowExecutionManager;
import com.unisoft.core.workflow.SequentialFlow;
import com.unisoft.core.workflow.Workflow;
import com.unisoft.core.workflow.engine.PublisherWorkflowEngineBuilder;
import com.unisoft.core.workflow.work.DefaultExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PublisherWorkflowEngineTest {

    static final Workflow workflow = new SequentialFlow.Builder()
            .name("conditional")
            .initially(context -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context))
            .build();

    @Test
    void builder() {
        final PublisherWorkflowEngineBuilder builder = new PublisherWorkflowEngineBuilder();
        assertThrows(NullPointerException.class, builder::build);

        builder.manager(new DefaultWorkflowExecutionManager());
        assertDoesNotThrow(builder::build);
    }

}