package com.unisoft.core.workflow.work;

import com.unisoft.core.util.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * implements an executable work that it's action execution result triggers a side job option work.
 * the executed side job runs asynchronously and does not affect the direction,
 * nor the context of the containing workflow execution.
 *
 * @author omar.H.Ajmi
 * @since 01/11/2020
 */
public class MultiOptionWork implements Work {
    private static final Logger log = LoggerFactory.getLogger(MultiOptionWork.class);

    private final Work action;
    private final Work onFail;
    private final Work onComplete;
    private final Work onSuspend;
    private final Work onCancel;

    MultiOptionWork(Work action, Work onFail, Work onComplete, Work onSuspend, Work onCancel) {
        this.action = Objects.requireNonNull(action, "'action' cannot be null");
        this.onFail = onFail;
        this.onComplete = onComplete;
        this.onSuspend = onSuspend;
        this.onCancel = onCancel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionReport execute(Context context) {
        final ExecutionReport report = this.action.execute(context);
        this.runOptionWork(report); // TODO see how to propagate execution report.
        return report;
    }

    /**
     * runs option work according to action execution result
     */
    private void runOptionWork(ExecutionReport report) {
        switch (report.status()) {
            case COMPLETED:
                this.runWork(report, this.onComplete);
                break;
            case FAILED:
                this.runWork(report, this.onFail);
                break;
            case CANCELED:
                this.runWork(report, this.onCancel);
                break;
            case PENDING:
                this.runWork(report, this.onSuspend);
                break;
            default:
                break;
        }
    }

    /**
     * run selected option work on a specific context.
     *
     * @param report     action execution report
     * @param optionWork selected option work to execute
     */
    private void runWork(ExecutionReport report, Work optionWork) {
        if (Objects.isNull(optionWork)) {
            log.warn("no option-work for status {} provided, skipping...", report.status());
        } else {
            CompletableFuture.supplyAsync(() -> {
                log.debug("running option-work for status {}", report.status());
                return optionWork.execute(report.context());
            })
                    .exceptionally(throwable -> {
                        log.error("error while executing option-work for status {}", report.status(), throwable);
                        return new DefaultExecutionReport(ExecutionStatus.FAILED, report.context(), throwable);
                    });
        }
    }

    public static class Builder {
        private Work action;
        private Work onFail;
        private Work onComplete;
        private Work onSuspend;
        private Work onCancel;

        public MultiOptionWork build() {
            return new MultiOptionWork(this.action, this.onFail, this.onComplete, this.onSuspend, this.onCancel);
        }

        public Builder action(Work action) {
            this.action = action;
            return this;
        }

        public Builder onFail(Work onFail) {
            this.onFail = onFail;
            return this;
        }

        public Builder onComplete(Work onComplete) {
            this.onComplete = onComplete;
            return this;
        }

        public Builder onSuspend(Work onSuspend) {
            this.onSuspend = onSuspend;
            return this;
        }

        public Builder onCancel(Work onCancel) {
            this.onCancel = onCancel;
            return this;
        }
    }
}
