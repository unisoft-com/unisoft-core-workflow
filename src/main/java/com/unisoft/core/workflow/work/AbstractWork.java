package com.unisoft.core.workflow.work;

import com.unisoft.core.util.Context;

/**
 * @author omar.H.Ajmi
 * @since 01/11/2020
 */
public abstract class AbstractWork implements Work {
    public static final String FAILED_MARKER = "$FAILED_WORKER";
    public static final String CANCELED_MARKER = "$CANCELED_WORKER";
    public static final String COMPLETED_MARKER = "$COMPLETED_WORKER";
    public static final String PENDED_MARKER = "$PENDED_WORKER";
    private final String id;

    protected AbstractWork() {
        this.id = Work.super.id();
    }

    @Override
    public String id() {
        return this.id;
    }

    /**
     * leaves {@link ExecutionStatus#FAILED} marker
     *
     * @param context execution context
     * @return marked context
     */
    protected Context markFailure(Context context) {
        return context.addData(FAILED_MARKER, this.id);
    }

    /**
     * leaves {@link ExecutionStatus#COMPLETED} marker
     *
     * @param context execution context
     * @return marked context
     */
    protected Context markCompletion(Context context) {
        return context.addData(COMPLETED_MARKER, this.id);
    }

    /**
     * leaves {@link ExecutionStatus#CANCELED} marker
     *
     * @param context execution context
     * @return marked context
     */
    protected Context markCancellation(Context context) {
        return context.addData(CANCELED_MARKER, this.id);
    }

    /**
     * leaves {@link ExecutionStatus#PENDING} marker
     *
     * @param context execution context
     * @return marked context
     */
    protected Context markPending(Context context) {
        return context.addData(PENDED_MARKER, this.id);
    }
}
