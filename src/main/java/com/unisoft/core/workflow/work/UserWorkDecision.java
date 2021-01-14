package com.unisoft.core.workflow.work;

/**
 * @author omar.H.Ajmi
 * @since 14/11/2020
 */
public enum UserWorkDecision {
    /**
     * user failed to execute work.
     */
    FAILED,
    /**
     * user successfully completed work.
     */
    COMPLETED,

    /**
     * user is pending work for some reason.
     */
    PENDING,

    /**
     * user canceled work for some reason.
     */
    CANCELED
}
