package com.unisoft.core.worklow.engine;

import com.unisoft.core.workflow.engine.BaseWorkflowEngineBuilder;
import com.unisoft.core.workflow.engine.WorkflowEngine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BaseWorkflowEngineBuilderTest {

    @Test
    void build() {
        final WorkflowEngine workflowEngine = assertDoesNotThrow(BaseWorkflowEngineBuilder::build);

        assertNotNull(workflowEngine);
    }
}