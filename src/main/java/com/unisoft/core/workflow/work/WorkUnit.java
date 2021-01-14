package com.unisoft.core.workflow.work;

import com.unisoft.core.util.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Function;

/**
 * a work unit definition that marks it's {@link ExecutionStatus} into execution {@link Context}.
 *
 * @author omar.H.Ajmi
 * @since 18/11/2020
 */
public class WorkUnit extends AbstractWork {
    private static final Logger log = LoggerFactory.getLogger(WorkUnit.class);

    private final Function<Context, ExecutionReport> workSupplier;

    public WorkUnit(Function<Context, ExecutionReport> workSupplier) {
        this.workSupplier = Objects.requireNonNull(workSupplier, "'work-supplier' cannot be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionReport execute(Context context) {
        ExecutionReport report = this.workSupplier.apply(context);

        switch (report.status()) {
            case CANCELED:
                report = new DefaultExecutionReport(report.status(), this.markCancellation(report.context()), report.error());
                break;
            case PENDING:
                report = new DefaultExecutionReport(report.status(), this.markPending(report.context()), report.error());
                break;
            case FAILED:
                report = new DefaultExecutionReport(report.status(), this.markFailure(report.context()), report.error());
                break;
            default:
                report = new DefaultExecutionReport(report.status(), this.markCompletion(report.context()), report.error());
                break;
        }
        return report;
    }
}
