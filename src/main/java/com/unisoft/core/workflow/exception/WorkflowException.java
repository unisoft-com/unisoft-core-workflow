package com.unisoft.core.workflow.exception;

import com.unisoft.core.exception.UnisoftException;

/**
 * @author omar.H.Ajmi
 * @since 29/10/2020
 */
public class WorkflowException extends UnisoftException {

    public WorkflowException(String message) {
        super(message);
    }

    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }
}
