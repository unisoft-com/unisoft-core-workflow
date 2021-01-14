package com.unisoft.core.worklow.work;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import com.unisoft.core.workflow.work.UserWork;
import com.unisoft.core.workflow.work.UserWorkDecision;
import com.unisoft.core.workflow.work.exception.UserDecisionMissingException;
import com.unisoft.core.workflow.work.exception.UserWorkDecisionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserWorkTest {

    static final UserWork userWork = new UserWork();

    static final ExecutionReport report = userWork.execute(new Context("key", "value"));
    ExecutionReport completedWorkReport;

    @BeforeEach
    void initialStatusIsPENDING() {
        assertEquals(ExecutionStatus.PENDING, report.status());
    }

    @AfterEach
    void didResume() {
        assertTrue(userWork.didResume());
    }

    @Test
    void completeAfterResume() {
        this.completedWorkReport = userWork.resume(new Context(UserWork.USER_DECISION_KEY, UserWorkDecision.COMPLETED));
        assertEquals(ExecutionStatus.COMPLETED, completedWorkReport.status());
    }

    @Test
    void failAfterResume() {
        this.completedWorkReport = userWork.resume(new Context(UserWork.USER_DECISION_KEY, UserWorkDecision.FAILED));
        assertEquals(ExecutionStatus.FAILED, completedWorkReport.status());
        assertTrue(completedWorkReport.error() instanceof UserWorkDecisionException);
    }

    @Test
    void pendAfterResume() {
        this.completedWorkReport = userWork.resume(new Context(UserWork.USER_DECISION_KEY, UserWorkDecision.PENDING));
        assertEquals(ExecutionStatus.PENDING, completedWorkReport.status());
        assertTrue(completedWorkReport.error() instanceof UserWorkDecisionException);
    }

    @Test
    void cancelAfterResume() {
        this.completedWorkReport = userWork.resume(new Context(UserWork.USER_DECISION_KEY, UserWorkDecision.CANCELED));
        assertEquals(ExecutionStatus.CANCELED, completedWorkReport.status());
        assertTrue(completedWorkReport.error() instanceof UserWorkDecisionException);
    }

    @Test
    void missingUserDecisionThrows() {
        final Context failureContext = new Context("MISSING_USER_DECISION_KEY", UserWorkDecision.COMPLETED);
        assertThrows(UserDecisionMissingException.class, () -> userWork.resume(failureContext));
    }
}