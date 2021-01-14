package com.unisoft.core.worklow;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.ParallelWorkflowReport;
import com.unisoft.core.workflow.work.DefaultExecutionReport;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParallelWorkflowReportTest {

    final static Context context = new Context("key", "value");
    static final DefaultExecutionReport successReport = new DefaultExecutionReport(ExecutionStatus.COMPLETED, context);
    static final DefaultExecutionReport anOtherSuccessReport = new DefaultExecutionReport(ExecutionStatus.COMPLETED, context);
    static final DefaultExecutionReport pendingReport = new DefaultExecutionReport(ExecutionStatus.PENDING, context);
    static final DefaultExecutionReport canceledReport = new DefaultExecutionReport(ExecutionStatus.CANCELED, context, new IllegalStateException());
    static final DefaultExecutionReport failReport = new DefaultExecutionReport(ExecutionStatus.FAILED, context, new IllegalStateException());

    @Test
    void statusFAILAndHasError() {
        final List<ExecutionReport> reports = Arrays.asList(successReport, anOtherSuccessReport, failReport);

        final ParallelWorkflowReport workflowReport = new ParallelWorkflowReport(reports);
        assertEquals(ExecutionStatus.FAILED, workflowReport.status());
        assertNotNull(workflowReport.error());
        assertEquals(context, workflowReport.context());
    }

    @Test
    void statusCOMPLETEDAndNoError() {
        final List<ExecutionReport> reports = Arrays.asList(successReport, anOtherSuccessReport, anOtherSuccessReport);

        final ParallelWorkflowReport workflowReport = new ParallelWorkflowReport(reports);
        assertEquals(ExecutionStatus.COMPLETED, workflowReport.status());
        assertNull(workflowReport.error());
        assertEquals(context, workflowReport.context());
    }

    @Test
    void statusPENDINGAndNoError() {
        final List<ExecutionReport> reports = Arrays.asList(successReport, pendingReport);

        final ParallelWorkflowReport workflowReport = new ParallelWorkflowReport(reports);
        assertEquals(ExecutionStatus.PENDING, workflowReport.status());
        assertNull(workflowReport.error());
        assertEquals(context, workflowReport.context());
    }

    @Test
    void statusCANCELEDAndHasError() {
        final List<ExecutionReport> reports = Arrays.asList(successReport, canceledReport);

        final ParallelWorkflowReport workflowReport = new ParallelWorkflowReport(reports);
        assertEquals(ExecutionStatus.CANCELED, workflowReport.status());
        assertNotNull(workflowReport.error());
        assertEquals(context, workflowReport.context());
    }
}