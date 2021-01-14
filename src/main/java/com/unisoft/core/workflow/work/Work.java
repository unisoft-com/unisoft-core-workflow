package com.unisoft.core.workflow.work;

import com.unisoft.core.util.Context;

import java.util.UUID;

/**
 * base executable unit contract.
 * each executable unit is executed with a data context
 * that determines its execution context, and a unique id.
 *
 * @author omar.H.Ajmi
 * @since 29/10/2020
 */
public interface Work {
    /**
     * execute work unit and returns its execution report, with execution status
     * and a reference to the error if execution has failed.
     *
     * @param context unit execution data context
     * @return execution report
     */
    ExecutionReport execute(Context context);

    default String id() {
        return UUID.randomUUID().toString();
    }
}
