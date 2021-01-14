package com.unisoft.core.worklow.testable;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.AbstractWorkflow;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.Work;

import java.util.Collections;

/**
 * this is a dummy workflow used for simple workflows testing
 *
 * @author omar.H.Ajmi
 * @since 16/11/2020
 */
public class DummyWorkflow extends AbstractWorkflow {
    private final Work workUnit;

    public DummyWorkflow(Work workUnit) {
        super("dummy-workflow");
        this.workUnit = workUnit;
    }

    @Override
    public Iterable<Work> workUnits() {
        return Collections.singletonList(this.workUnit);
    }

    @Override
    public ExecutionReport execute(Context context) {
        return this.workUnit.execute(context);
    }
}
