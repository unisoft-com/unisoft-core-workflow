package com.unisoft.core.workflow.work.exception;

/**
 * exception that indicates a {@link com.unisoft.workflow.core.work.UserWorkDecision}
 * is missing from work context
 *
 * @author omar.H.Ajmi
 * @since 14/11/2020
 */
public class UserDecisionMissingException extends WorkException {
    public UserDecisionMissingException() {
        super();
    }

    public UserDecisionMissingException(String message) {
        super(message);
    }
}
