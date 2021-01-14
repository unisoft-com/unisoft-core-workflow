package com.unisoft.core.worklow.work;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.work.DefaultExecutionReport;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import com.unisoft.core.workflow.work.WorkUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkUnitTest {

    private static final Context testContext = new Context("", "");

    @Test
    void instantiate() {
        assertDoesNotThrow(() -> new WorkUnit(context -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context)));
        assertThrows(NullPointerException.class, () -> new WorkUnit(null));
    }

    @Test
    void failMarkerExists() {
        final WorkUnit workUnit = new WorkUnit(context -> new DefaultExecutionReport(ExecutionStatus.FAILED, context));
        final ExecutionReport report = workUnit.execute(testContext);
        this.markerExistsAndValid(report.context(), workUnit.id(), WorkUnit.FAILED_MARKER);
    }

    @Test
    void completeMarkerExists() {
        final WorkUnit workUnit = new WorkUnit(context -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context));
        final ExecutionReport report = workUnit.execute(testContext);
        this.markerExistsAndValid(report.context(), workUnit.id(), WorkUnit.COMPLETED_MARKER);
    }

    @Test
    void canceledMarkerExists() {
        final WorkUnit workUnit = new WorkUnit(context -> new DefaultExecutionReport(ExecutionStatus.CANCELED, context));
        final ExecutionReport report = workUnit.execute(testContext);
        this.markerExistsAndValid(report.context(), workUnit.id(), WorkUnit.CANCELED_MARKER);
    }

    @Test
    void pendingMarkerExists() {
        final WorkUnit workUnit = new WorkUnit(context -> new DefaultExecutionReport(ExecutionStatus.PENDING, context));
        final ExecutionReport report = workUnit.execute(testContext);
        this.markerExistsAndValid(report.context(), workUnit.id(), WorkUnit.PENDED_MARKER);
    }

    private void markerExistsAndValid(Context context, String value, String marker) {
        assertTrue(context.getData(marker).isPresent());
        assertEquals(value, context.getData(marker, String.class).get());
    }
}