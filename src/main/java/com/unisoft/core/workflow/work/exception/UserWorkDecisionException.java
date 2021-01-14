package com.unisoft.core.workflow.work.exception;

import com.unisoft.core.workflow.work.UserWorkDecision;

/**
 * exception indicates that user decided to block a work item
 * by:
 * <p>
 *     <ul>
 *         <li>pending it</li>
 *         <li>canceling it</li>
 *         <li>failing it</li>
 *     </ul>
 * </p>
 *
 * @author omar.H.Ajmi
 * @since 14/11/2020
 */
public class UserWorkDecisionException extends WorkException {
    public UserWorkDecisionException(UserWorkDecision decision) {
        super("user decision: " + decision);
    }
}
