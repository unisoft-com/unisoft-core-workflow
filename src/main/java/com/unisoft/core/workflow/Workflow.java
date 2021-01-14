package com.unisoft.core.workflow;

import com.unisoft.core.workflow.work.Work;

/**
 * generic workflow contract.
 *
 * @author omar.H.Ajmi
 * @since 29/10/2020
 */
public interface Workflow extends Work {

    /**
     * gets the workflow name
     *
     * @return workflow name
     */
    String name();

    /**
     * gets workflow work units
     *
     * @return
     */
    Iterable<Work> workUnits();
}
