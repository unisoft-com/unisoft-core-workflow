package com.unisoft.core.worklow;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.SequentialFlow;
import com.unisoft.core.workflow.exception.WorkflowException;
import com.unisoft.core.workflow.work.DefaultExecutionReport;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import com.unisoft.core.workflow.work.Work;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SequentialFlowTest {

    static String INITIAL_KEY = "key";
    static String INITIAL_VALUE = "value";

    static String EXECUTION_KEY = "new key";
    static String EXECUTION_VALUE = "new value";

    static String WORKFLOW_NAME = "sequential";

    static Work workUnit = context -> context.getData(INITIAL_KEY)
            .map(value -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context.addData(EXECUTION_KEY, EXECUTION_VALUE)))
            .orElse(new DefaultExecutionReport(ExecutionStatus.FAILED, context, new IllegalStateException("data not found")));

    @Test
    void buildFails() {

        final SequentialFlow.Builder builder = new SequentialFlow.Builder();
        assertThrows(NullPointerException.class, builder::build);

        builder.name(WORKFLOW_NAME);
        assertThrows(WorkflowException.class, () -> builder.name(WORKFLOW_NAME).then(workUnit).build());
    }

    @Test
    void buildSuccess() {
        Work otherWorkUnit = context -> null;
        final SequentialFlow.Builder builder = new SequentialFlow.Builder().name(WORKFLOW_NAME);
        final SequentialFlow sequentialFlow = assertDoesNotThrow(() -> builder.name(WORKFLOW_NAME).initially(workUnit).then(otherWorkUnit).build());
        assertNotNull(sequentialFlow);
        assertEquals(2, sequentialFlow.size());
        assertEquals(2, sequentialFlow.size());
    }

    @Test
    void executeSuccess() {
        final Context context = new Context(INITIAL_KEY, INITIAL_VALUE);
        final SequentialFlow.Builder builder = new SequentialFlow.Builder().name(WORKFLOW_NAME);
        final SequentialFlow sequential = builder.name(WORKFLOW_NAME).initially(workUnit).build();
        final ExecutionReport executionReport = sequential.execute(context);
        assertEquals(ExecutionStatus.COMPLETED, executionReport.status());
        assertTrue(executionReport.context().getData(EXECUTION_KEY).isPresent());
        assertEquals(EXECUTION_VALUE, executionReport.context().getData(EXECUTION_KEY).get());
    }

    @Test
    void executeFail() {
        final Context context = new Context("INITIAL_KEY", "INITIAL_VALUE");
        final SequentialFlow.Builder builder = new SequentialFlow.Builder().name(WORKFLOW_NAME);
        final SequentialFlow sequential = builder.name(WORKFLOW_NAME).initially(workUnit).build();
        final ExecutionReport report = sequential.execute(context);
        assertEquals(ExecutionStatus.FAILED, report.status());
        assertTrue(report.error() instanceof IllegalStateException);
    }

    @Test
    void executionPending() {
        final Context context = new Context("INITIAL_KEY", "INITIAL_VALUE");
        final SequentialFlow pendingSequentialFlow = new SequentialFlow.Builder()
                .name(WORKFLOW_NAME)
                .initially(ctxt -> new DefaultExecutionReport(ExecutionStatus.PENDING, ctxt))
                .build();
        final ExecutionReport report = pendingSequentialFlow.execute(context);
        assertEquals(ExecutionStatus.PENDING, report.status());
    }
}