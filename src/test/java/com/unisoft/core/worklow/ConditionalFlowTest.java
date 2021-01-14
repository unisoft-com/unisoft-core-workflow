package com.unisoft.core.worklow;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.ConditionalFlow;
import com.unisoft.core.workflow.work.DefaultExecutionReport;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConditionalFlowTest {
    static final ConditionalFlow.Builder builder = new ConditionalFlow.Builder();
    private static final String WORKFLOW_NAME = "conditional";
    static String INITIAL_KEY = "key";
    static String INITIAL_VALUE = "value";
    static String EXECUTION_KEY = "new key";
    static String EXECUTION_VALUE = "new value";
    static final ConditionalFlow conditionalFlow = builder.name(WORKFLOW_NAME)
            .initially(context -> context.getData(INITIAL_KEY)
                    .map(value -> {
                        context.addData(EXECUTION_KEY, EXECUTION_VALUE);
                        return new DefaultExecutionReport(ExecutionStatus.COMPLETED, context);
                    })
                    .orElse(new DefaultExecutionReport(ExecutionStatus.FAILED, context)))
            .onSuccess(context -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context.addData("success_key", "success_value")))
            .onFail(context -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context.addData("fail_key", "fail_value")))
            .predicate(report -> report.status() == ExecutionStatus.COMPLETED)
            .build();

    @Test
    void build() {
        final ConditionalFlow.Builder builder = new ConditionalFlow.Builder();

        assertThrows(NullPointerException.class, () -> builder.build());
    }

    @Test
    void successWorkUnitExecuted() {
        final ExecutionReport executionReport = conditionalFlow.execute(new Context(INITIAL_KEY, INITIAL_VALUE));
        assertEquals("success_value", executionReport.context().getData("success_key").get());
    }

    @Test
    void failWorkUnitExecuted() {
        final ExecutionReport executionReport = conditionalFlow.execute(new Context("INITIAL_KEY", "INITIAL_VALUE"));
        assertEquals("fail_value", executionReport.context().getData("fail_key").get());
    }

    @Test
    void terminateOnInitialWorkPending() {
        final ConditionalFlow pendingConditionalFlow = builder.name(WORKFLOW_NAME)
                .initially(context -> context.getData(INITIAL_KEY)
                        .map(value -> new DefaultExecutionReport(ExecutionStatus.PENDING, context))
                        .orElse(new DefaultExecutionReport(ExecutionStatus.FAILED, context)))
                .onSuccess(context -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context.addData("success_key", "success_value")))
                .onFail(context -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context.addData("fail_key", "fail_value")))
                .predicate(report -> report.status() == ExecutionStatus.COMPLETED)
                .build();
        final ExecutionReport pendingReport = pendingConditionalFlow.execute(new Context(INITIAL_KEY, INITIAL_VALUE));
        assertEquals(ExecutionStatus.PENDING, pendingReport.status());
    }
}