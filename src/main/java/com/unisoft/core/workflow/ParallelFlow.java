package com.unisoft.core.workflow;

import com.unisoft.core.annotations.Immutable;
import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.work.Work;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * a parallel workflow that executes its units in a parallel fashion
 * while ignoring its order of insertion.
 *
 * @author omar.H.Ajmi
 * @since 29/10/2020
 */
@Immutable
public class ParallelFlow extends AbstractWorkflow {
    private final List<Work> workUnits = new ArrayList<>();
    private final ParallelFlowRunner runner;

    ParallelFlow(String name, List<Work> workUnits, ParallelFlowRunner runner) {
        super(name);
        this.runner = Objects.requireNonNull(runner, "'parallel runner' cannot be null");
        this.workUnits.addAll(workUnits);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParallelWorkflowReport execute(Context context) {
        return new ParallelWorkflowReport(this.runner.run(this.workUnits, context));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Work> workUnits() {
        return this.workUnits;
    }

    public static class Builder {
        private final List<Work> workUnits = new ArrayList<>();
        private ExecutorService runner;
        private String name;

        /**
         * builds a new {@code ParallelFlow} instance
         *
         * @return a new {@code ParallelFlow}
         */
        public ParallelFlow build() {
            return new ParallelFlow(this.name, this.workUnits, new ParallelFlowRunner(this.runner));
        }

        /**
         * set the parallel workflow name
         *
         * @param name sequential workflow name
         * @return {@code Builder}
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * set the parallel runner of the work units
         *
         * @param runner parallel runner
         * @return {@code Builder}
         */
        public Builder runner(ExecutorService runner) {
            this.runner = runner;
            return this;
        }

        /**
         * add a work unit to the parallel work units
         *
         * @param workUnit work unit to add
         * @return {@code Builder}
         */
        public Builder workUnit(Work workUnit) {
            this.workUnits.add(workUnit);
            return this;
        }
    }
}
