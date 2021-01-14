package com.unisoft.core.workflow;

import com.unisoft.core.annotations.Immutable;
import com.unisoft.core.util.CoreUtil;
import com.unisoft.core.workflow.work.AbstractWork;

/**
 * base workflow unit.
 *
 * @author omar.H.Ajmi
 * @since 29/10/2020
 */
@Immutable
public abstract class AbstractWorkflow extends AbstractWork implements Workflow {

    private final String name;

    /**
     * any workflow requires at least the name.
     *
     * @param name name of the workflow
     */
    public AbstractWorkflow(String name) {
        CoreUtil.requireNonNullOrEmpty(name, "'name' cannot be empty");
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }
}
