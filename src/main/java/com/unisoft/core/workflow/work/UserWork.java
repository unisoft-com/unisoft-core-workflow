package com.unisoft.core.workflow.work;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.work.exception.UserDecisionMissingException;
import com.unisoft.core.workflow.work.exception.UserWorkDecisionException;

/**
 * basic user work definition.
 * returns pending execution report when reached for first time.
 * if has been resumed then returns user decision and data as report.
 *
 * @author omar.H.Ajmi
 * @since 14/11/2020
 */
public class UserWork extends AbstractWork {
    public static final String USER_DECISION_KEY = "$USER_DECISION";
    private boolean didResume;

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionReport execute(Context context) {
        if (!this.didResume) {
            return new DefaultExecutionReport(ExecutionStatus.PENDING, context);
        }
        return context.getData(USER_DECISION_KEY, UserWorkDecision.class)
                .map(decision -> {
                    switch (decision) {
                        case CANCELED:
                            return new DefaultExecutionReport(ExecutionStatus.CANCELED, context, new UserWorkDecisionException(decision));
                        case PENDING:
                            return new DefaultExecutionReport(ExecutionStatus.PENDING, context, new UserWorkDecisionException(decision));
                        case FAILED:
                            return new DefaultExecutionReport(ExecutionStatus.FAILED, context, new UserWorkDecisionException(decision));
                        case COMPLETED:
                            return new DefaultExecutionReport(ExecutionStatus.COMPLETED, context);
                        default:
                            return new DefaultExecutionReport(ExecutionStatus.CANCELED, context, new IllegalArgumentException("could not parse user decision"));
                    }
                })
                .orElseThrow(UserDecisionMissingException::new);
    }

    public ExecutionReport resume(Context context) {
        this.didResume = true;
        return this.execute(context);
    }

    /**
     * determines if work did resume
     *
     * @return true if work did resume | false if not
     */
    public boolean didResume() {
        return this.didResume;
    }
}
