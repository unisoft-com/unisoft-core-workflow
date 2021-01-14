package com.unisoft.core.worklow.work;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.work.DefaultExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultExecutionReportTest {

    @Test
    void instantiate() {
        assertThrows(NullPointerException.class, () -> new DefaultExecutionReport(null, new Context("", "")));
        assertThrows(NullPointerException.class, () -> new DefaultExecutionReport(ExecutionStatus.PENDING, null));
        assertThrows(NullPointerException.class, () -> new DefaultExecutionReport(null, null));
        assertDoesNotThrow(() -> new DefaultExecutionReport(ExecutionStatus.PENDING, new Context("", "")));
    }

    @Test
    void toStringMatches() {
        final DefaultExecutionReport reportWithError = new DefaultExecutionReport(ExecutionStatus.PENDING, new Context("", ""), new RuntimeException());
        String expected = "DefaultExecutionReport{" +
                ", status=" + reportWithError.status() +
                ", context=" + reportWithError.context().toString() +
                ", error=" + reportWithError.error() + "}";

        assertEquals(expected, reportWithError.toString());

        final DefaultExecutionReport reportWithoutError = new DefaultExecutionReport(ExecutionStatus.PENDING, new Context("", ""));
        expected = "DefaultExecutionReport{" +
                ", status=" + reportWithoutError.status() +
                ", context=" + reportWithoutError.context().toString() + "}";

        assertEquals(expected, reportWithoutError.toString());
    }
}