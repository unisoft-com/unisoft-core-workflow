package com.unisoft.core.workflow;

import com.unisoft.core.annotations.Immutable;
import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.Work;
import com.unisoft.core.workflow.work.predicate.WorkPredicate;

import java.util.Collections;
import java.util.Objects;

/**
 * a looped workflow that executes a single work unit until a predicate is proven.
 *
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
@Immutable
public class RepeatFlow extends AbstractWorkflow {
    private final Work work;
    private final WorkPredicate predicate;

    public RepeatFlow(String name, Work work, WorkPredicate predicate) {
        super(name);
        this.work = Objects.requireNonNull(work, "'work' cannot be null");
        this.predicate = Objects.requireNonNull(predicate, "'predicate' cannot be null");
    }

    @Override
    public ExecutionReport execute(Context context) {
        ExecutionReport report = null;
        do {
            report = (report != null) ? this.runWorkUnit(report.context()) : this.runWorkUnit(context);
        } while (this.predicate.apply(report));
        return report;
    }

    private ExecutionReport runWorkUnit(Context context) {
        return this.work.execute(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Work> workUnits() {
        return Collections.singletonList(this.work);
    }

    public static class Builder {
        private String name;
        private Work work;
        private WorkPredicate predicate;

        /**
         * builds a new {@code RepeatFlow} instance
         *
         * @return a new {@code RepeatFlow}
         */
        public RepeatFlow build() {
            return new RepeatFlow(this.name, this.work, this.predicate);
        }

        /**
         * set the workflow name
         *
         * @param name workflow name
         * @return {@code Builder}
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * set the workflow work unit
         *
         * @param work work unit
         * @return {@code Builder}
         */
        public Builder work(Work work) {
            this.work = work;
            return this;
        }

        /**
         * set the workflow predicate
         *
         * @param predicate workflow predicate that keeps the loop running
         * @return {@code Builder}
         */
        public Builder predicate(WorkPredicate predicate) {
            this.predicate = predicate;
            return this;
        }
    }
}
