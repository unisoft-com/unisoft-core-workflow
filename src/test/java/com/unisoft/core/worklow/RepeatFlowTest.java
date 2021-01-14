package com.unisoft.core.worklow;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.RepeatFlow;
import com.unisoft.core.workflow.work.DefaultExecutionReport;
import com.unisoft.core.workflow.work.ExecutionReport;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static com.unisoft.core.workflow.work.ExecutionStatus.COMPLETED;
import static com.unisoft.core.workflow.work.ExecutionStatus.FAILED;
import static org.junit.jupiter.api.Assertions.*;

class RepeatFlowTest {

    private static final String WORKFLOW_NAME = "repeat";
    private static final String INITIAL_KEY = "key";
    private static final String EXECUTION_KEY = "execution_key";

    @Test
    void build() {
        final RepeatFlow.Builder builder = new RepeatFlow.Builder();
        assertThrows(NullPointerException.class, builder::build);

        builder.name(WORKFLOW_NAME);
        assertThrows(NullPointerException.class, builder::build);

        builder.work(context -> new DefaultExecutionReport(COMPLETED, context));
        assertThrows(NullPointerException.class, builder::build);

        builder.predicate(report -> report.status() != COMPLETED);
        assertDoesNotThrow(builder::build);
    }

    @Test
    void repeatWhile() {

        final RepeatFlow.Builder builder = new RepeatFlow.Builder();
        builder.name(WORKFLOW_NAME)
                .work(context ->
                        context.getData(INITIAL_KEY).map(initialValue ->
                                context.getData(EXECUTION_KEY).map(executionValue -> new DefaultExecutionReport(COMPLETED, context.addData(EXECUTION_KEY, Integer.valueOf((Integer) executionValue) + 1)))
                                        .orElse(new DefaultExecutionReport(COMPLETED, context.addData(EXECUTION_KEY, Integer.valueOf((Integer) initialValue) + 1)))
                        ).orElse(new DefaultExecutionReport(FAILED, context, new NoSuchElementException(EXECUTION_KEY)))
                )
                .predicate(report -> report.status() == COMPLETED && (report.context().getData(EXECUTION_KEY).isPresent() && (Integer) report.context().getData(EXECUTION_KEY).get() < 10));

        final RepeatFlow repeatFlow = assertDoesNotThrow(builder::build);
        final ExecutionReport executionReport = repeatFlow.execute(new Context(INITIAL_KEY, 0));
        assertTrue(executionReport.context().getData(EXECUTION_KEY).isPresent());
        assertEquals(10, executionReport.context().getData(EXECUTION_KEY).get());
    }
}