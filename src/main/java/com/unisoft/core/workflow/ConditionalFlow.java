package com.unisoft.core.workflow;

import com.unisoft.core.annotations.Immutable;
import com.unisoft.core.util.Context;
import com.unisoft.core.util.log.LogUtil;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import com.unisoft.core.workflow.work.Work;
import com.unisoft.core.workflow.work.predicate.WorkPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
@Immutable
public class ConditionalFlow extends AbstractWorkflow {

    private static final Logger log = LoggerFactory.getLogger(ConditionalFlow.class);

    private final Work initialWorkUnit;
    private final Work onSuccessWorkUnit;
    private final Work onFailWorkUnit;

    private final WorkPredicate predicate;

    ConditionalFlow(String name, Work initialWorkUnit, Work onSuccessWorkUnit, Work onFailWorkUnit, WorkPredicate predicate) {
        super(name);
        this.initialWorkUnit = Objects.requireNonNull(initialWorkUnit, "'initial-workunit' cannot be null");
        this.onSuccessWorkUnit = Objects.requireNonNull(onSuccessWorkUnit, "'on-success-workunit' cannot be null");
        this.onFailWorkUnit = Objects.requireNonNull(onFailWorkUnit, "'on-fail-workunit' cannot be null");
        this.predicate = Objects.requireNonNull(predicate, "'predicate' cannot be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionReport execute(Context context) {
        final ExecutionReport initialReport = this.initialWorkUnit.execute(context);

        if (initialReport.status() == ExecutionStatus.PENDING) {
            LogUtil.warn(log, "initial work status is {}, ...terminating conditional flow", initialReport.status());
            return initialReport;
        }

        if (this.predicate.apply(initialReport)) {
            return this.onSuccessWorkUnit.execute(initialReport.context());
        } else {
            return this.onFailWorkUnit.execute(initialReport.context());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Work> workUnits() {
        return Arrays.asList(this.initialWorkUnit, this.onSuccessWorkUnit, this.onFailWorkUnit);
    }

    /**
     * builder object for {@code ConditionalFlow} class
     */
    public static class Builder {
        private String name;
        private Work initialWorkUnit;
        private Work onSuccessWorkUnit;
        private Work onFailWorkUnit;
        private WorkPredicate predicate;

        /**
         * builds new {@code ConditionalFlow} instance
         *
         * @return {@code ConditionalFlow}
         */
        public ConditionalFlow build() {
            return new ConditionalFlow(this.name,
                    this.initialWorkUnit,
                    this.onSuccessWorkUnit,
                    this.onFailWorkUnit,
                    this.predicate);
        }

        /**
         * sets the workflow name
         *
         * @return {@code Builder}
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * sets initial work unit
         *
         * @return {@code Builder}
         */
        public Builder initially(Work workUnit) {
            this.initialWorkUnit = workUnit;
            return this;
        }

        /**
         * sets on success work unit
         *
         * @return {@code Builder}
         */
        public Builder onSuccess(Work workUnit) {
            this.onSuccessWorkUnit = workUnit;
            return this;
        }

        /**
         * sets on fail work unit
         *
         * @return {@code Builder}
         */
        public Builder onFail(Work workUnit) {
            this.onFailWorkUnit = workUnit;
            return this;
        }

        /**
         * sets conditional predicate
         *
         * @return {@code Builder}
         */
        public Builder predicate(WorkPredicate predicate) {
            this.predicate = predicate;
            return this;
        }
    }
}
