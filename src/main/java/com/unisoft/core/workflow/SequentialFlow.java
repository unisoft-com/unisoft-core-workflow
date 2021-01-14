package com.unisoft.core.workflow;

import com.unisoft.core.annotations.Immutable;
import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.exception.WorkflowException;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import com.unisoft.core.workflow.work.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * a sequential workflow that executes its units sequentially while
 * respecting its order of insertion.
 *
 * @author omar.H.Ajmi
 * @since 29/10/2020
 */
@Immutable
public class SequentialFlow extends AbstractWorkflow {

    private static final Logger log = LoggerFactory.getLogger(SequentialFlow.class);

    private final Set<Work> workUnits = new LinkedHashSet<>();

    SequentialFlow(String name, Set<Work> workUnits) {
        super(name);
        this.workUnits.addAll(workUnits);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionReport execute(Context context) {
        ExecutionReport report = null;
        for (Work workUnit : this.workUnits) {
            report = (report != null) ? workUnit.execute(report.context()) : workUnit.execute(context);
            if (report.status() == ExecutionStatus.FAILED || report.status() == ExecutionStatus.CANCELED) {
                log.warn("execution aborted, unit {} has {} ...skipping subsequent work units.", workUnit.id(), report.status());
                break;
            } else if (report.status() == ExecutionStatus.PENDING) {
                log.warn("execution stopped, unit {} is {} ...skipping subsequent work units.", workUnit.id(), report.status());
                break;
            }
        }

        return report;
    }

    /**
     * gets work units count
     *
     * @return work units count
     */
    public int size() {
        return this.workUnits.size();
    }

    /**
     * gets the workflow work units in the order of the insertion.
     *
     * @return workflow work units
     */
    @Override
    public Iterable<Work> workUnits() {
        return this.workUnits;
    }

    /**
     * builder object for {@code SequentialFlow}
     */
    public static class Builder {
        private Set<Work> workUnits;
        private String name;

        /**
         * builds a new {@code SequentialFlow} instance
         *
         * @return a new {@code SequentialFlow}
         */
        public SequentialFlow build() {
            return new SequentialFlow(this.name, this.workUnits);
        }

        /**
         * set the sequential workflow name
         *
         * @param name sequential workflow name
         * @return {@code Builder}
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * sets the initial work unit
         *
         * @param workUnit initial work unit
         * @return {@code Builder}
         */
        public Builder initially(Work workUnit) {
            this.workUnits = new LinkedHashSet<>();
            this.workUnits.add(workUnit);
            return this;
        }

        /**
         * adds more work unit the the sequential workflow.
         *
         * @param workUnit work unit to add
         * @return
         */
        public Builder then(Work workUnit) {
            if (this.workUnits == null) {
                throw new WorkflowException("no initial work unit found for sequential-workflow.");
            }
            this.workUnits.add(workUnit);
            return this;
        }
    }
}
