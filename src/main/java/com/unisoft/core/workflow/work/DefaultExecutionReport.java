package com.unisoft.core.workflow.work;

import com.unisoft.core.util.Context;

import java.util.Objects;

/**
 * default execution report.
 * any execution report requires at least a status and the context of the execution
 *
 * @author omar.H.Ajmi
 * @since 29/10/2020
 */
public class DefaultExecutionReport implements ExecutionReport {
    private final ExecutionStatus status;
    private final Context context;
    private Throwable error = null;

    /**
     * execution report with minimum required params.
     *
     * @param status  status of the execution
     * @param context context of the execution
     */
    public DefaultExecutionReport(ExecutionStatus status, Context context) {
        this.status = Objects.requireNonNull(status, "'status' cannot be null");
        this.context = Objects.requireNonNull(context, "'context' cannot be null");
    }

    /**
     * execution report with error reference in case of error.
     *
     * @param status  status of the execution
     * @param context context of the execution
     * @param error   error reference
     */
    public DefaultExecutionReport(ExecutionStatus status, Context context, Throwable error) {
        this(status, context);
        this.error = error;
    }

    @Override
    public ExecutionStatus status() {
        return this.status;
    }

    @Override
    public Throwable error() {
        return this.error;
    }

    public void error(Throwable error) {
        this.error = error;
    }

    @Override
    public Context context() {
        return this.context;
    }

    @Override
    public String toString() {
        String toString = "DefaultExecutionReport{" +
                ", status=" + this.status +
                ", context=" + this.context.toString();
        toString += this.error != null ? ", error=" + this.error + "}" : "}";
        return toString;
    }
}
