package com.unisoft.core.workflow.work;

import com.unisoft.core.util.Context;

/**
 * base work unit execution report contract.
 *
 * @author omar.H.Ajmi
 * @since 29/10/2020
 */
public interface ExecutionReport {

    ExecutionStatus status();

    Throwable error();

    Context context();
}
