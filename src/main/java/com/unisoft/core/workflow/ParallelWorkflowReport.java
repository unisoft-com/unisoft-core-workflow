package com.unisoft.core.workflow;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * container for {@code com.unisoft.workflow.core.workflow.ParallelFlow} reports
 *
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
public class ParallelWorkflowReport implements ExecutionReport {
    private final List<ExecutionReport> reports = new ArrayList<>();

    public ParallelWorkflowReport(List<ExecutionReport> reports) {
        this.reports.addAll(reports);
    }

    /**
     * return execution reports
     */
    public List<ExecutionReport> reports() {
        return this.reports;
    }

    /**
     * if non of the execution reports has status as
     * {@code ExecutionStatus.FAILED} or
     * {@code ExecutionStatus.PENDING} or
     * {@code ExecutionStatus.CANCELED} then it is a {@code ExecutionStatus.COMPLETE}
     */
    @Override
    public ExecutionStatus status() {
        for (ExecutionReport report : this.reports) {
            switch (report.status()) {
                case FAILED:
                    return ExecutionStatus.FAILED;
                case PENDING:
                    return ExecutionStatus.PENDING;
                case CANCELED:
                    return ExecutionStatus.CANCELED;
                default:
                    break;
            }
        }
        return ExecutionStatus.COMPLETED;
    }

    /**
     * fetches first error of the execution reports
     */
    @Override
    public Throwable error() {
        for (ExecutionReport report : this.reports) {
            if (report.error() != null) {
                return report.error();
            }
        }
        return null;
    }

    /**
     * return last report context as all reports contexts are nested in the last one
     */
    @Override
    public Context context() {
        return this.reports.get(this.reports.size() - 1).context();
    }
}
