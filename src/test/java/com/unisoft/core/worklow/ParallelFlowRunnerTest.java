package com.unisoft.core.worklow;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.ParallelFlow;
import com.unisoft.core.workflow.ParallelWorkflowReport;
import com.unisoft.core.workflow.exception.WorkflowException;
import com.unisoft.core.workflow.work.DefaultExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import com.unisoft.core.workflow.work.Work;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class ParallelFlowRunnerTest {
    static String INITIAL_KEY = "key";
    static String INITIAL_VALUE = "value";

    static String EXECUTION_KEY = "new key";
    static String EXECUTION_VALUE = "new value";

    static String WORKFLOW_NAME = "parallel";
    static Work failingWorkUnit = context -> {
        throw new IllegalArgumentException();
    };

    static Work workUnit = context -> context.getData(INITIAL_KEY)
            .map(value -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context.addData(EXECUTION_KEY, EXECUTION_VALUE)))
            .orElse(new DefaultExecutionReport(ExecutionStatus.FAILED, context, new IllegalStateException("data not found")));

    private ExecutorService executorService;

    @BeforeEach
    void setupExecutor() {
        this.executorService = Executors.newFixedThreadPool(2);
    }

    @AfterEach
    void executorServiceIsDown() {
        assertTrue(this.executorService.isShutdown());
    }

    @Test
    void failedRun() {
        final Context context = new Context(INITIAL_KEY, INITIAL_VALUE);
        final ParallelFlow failedParallelFlow = new ParallelFlow.Builder()
                .name(WORKFLOW_NAME)
                .runner(this.executorService)
                .workUnit(workUnit)
                .workUnit(failingWorkUnit)
                .build();

        assertThrows(WorkflowException.class, () -> failedParallelFlow.execute(context));
    }

    @Test
    void successfulRun() {
        final Context context = new Context(INITIAL_KEY, INITIAL_VALUE);
        final ParallelFlow successfulParallelFlow = new ParallelFlow.Builder()
                .name(WORKFLOW_NAME)
                .runner(this.executorService)
                .workUnit(workUnit)
                .build();
        final ParallelWorkflowReport successfulReport = successfulParallelFlow.execute(context);

        assertEquals(1, successfulReport.reports().size());
        assertEquals(ExecutionStatus.COMPLETED, successfulReport.status());
        assertTrue(successfulReport.context().getData(EXECUTION_KEY).isPresent());
        assertEquals(EXECUTION_VALUE, successfulReport.context().getData(EXECUTION_KEY).get());
    }


}