package com.unisoft.core.workflow.work;

/**
 * @author omar.H.Ajmi
 * @since 29/10/2020
 */
public enum ExecutionStatus {

    /**
     * failed execution.
     */
    FAILED,
    /**
     * successfully completed execution.
     */
    COMPLETED,

    /**
     * execution is pending for some reason.
     */
    PENDING,

    /**
     * execution canceled for some reason.
     */
    CANCELED;

    /**
     * determines if status is a failure
     *
     * @return true if FAILED or CANCELED
     */
    public boolean isAFailure() {
        return this.equals(FAILED) || this.equals(CANCELED);
    }
}
