package com.unisoft.core.worklow;

import com.google.common.eventbus.Subscribe;
import com.unisoft.core.event.Event;
import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.DefaultWorkflowExecutionManager;
import com.unisoft.core.workflow.Workflow;
import com.unisoft.core.workflow.engine.PublisherWorkflowEngineBuilder;
import com.unisoft.core.workflow.engine.WorkflowEngine;
import com.unisoft.core.workflow.work.DefaultExecutionReport;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import com.unisoft.core.workflow.work.Work;
import com.unisoft.core.worklow.testable.DummyWorkflow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class DefaultWorkflowExecutionManagerTest {
    static final TestDefaultWorkflowExecutionManager listener = new TestDefaultWorkflowExecutionManager();
    static final Context context = mock(Context.class);

    static final WorkflowEngine engine = new PublisherWorkflowEngineBuilder()
            .manager(listener)
            .build();

    @Test
    void onStart() {
        Work work = ctxt -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, ctxt);
        final Workflow workflow = new DummyWorkflow(work);

        engine.run(workflow, context);
        assertTrue(listener.isOnStartedInvoked());
    }

    @Test
    void onComplete() {
        Work work = ctxt -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, ctxt);
        final Workflow workflow = new DummyWorkflow(work);

        engine.run(workflow, context);
        assertTrue(listener.isOnStartedInvoked());
        assertTrue(listener.isOnCompleteInvoked());
    }

    @Test
    void onCancel() {
        Work work = ctxt -> new DefaultExecutionReport(ExecutionStatus.CANCELED, ctxt);
        final Workflow workflow = new DummyWorkflow(work);

        engine.run(workflow, context);
        assertTrue(listener.isOnStartedInvoked());
        assertTrue(listener.isOnCancelInvoked());
    }

    @Test
    void onFail() {
        Work work = ctxt -> new DefaultExecutionReport(ExecutionStatus.FAILED, ctxt);
        final Workflow workflow = new DummyWorkflow(work);

        engine.run(workflow, context);
        assertTrue(listener.isOnStartedInvoked());
        assertTrue(listener.isOnFailedInvoked());
    }

    @Test
    void onPending() {
        Work work = ctxt -> new DefaultExecutionReport(ExecutionStatus.PENDING, ctxt);
        final Workflow workflow = new DummyWorkflow(work);

        engine.run(workflow, context);
        assertTrue(listener.isOnStartedInvoked());
        assertTrue(listener.isOnPendingInvoked());
    }

    public static class TestDefaultWorkflowExecutionManager extends DefaultWorkflowExecutionManager {
        private boolean onCompleteInvoked;
        private boolean onStartedInvoked;
        private boolean onCancelInvoked;
        private boolean onPendingInvoked;
        private boolean onFailedInvoked;

        @Subscribe
        @Override
        public void handle(Event event) {
            super.handle(event);
        }

        @Override
        public void onFail(ExecutionReport report, Workflow workflow) {
            super.onFail(report, workflow);
            this.onFailedInvoked = true;
        }

        @Override
        public void onComplete(ExecutionReport report, Workflow workflow) {
            super.onComplete(report, workflow);
            this.onCompleteInvoked = true;
        }

        @Override
        public void onCancel(ExecutionReport report, Workflow workflow) {
            super.onCancel(report, workflow);
            this.onCancelInvoked = true;
        }

        @Override
        public void onPending(ExecutionReport report, Workflow workflow) {
            super.onPending(report, workflow);
            this.onPendingInvoked = true;
        }

        @Override
        public void onStart(ExecutionReport report, Workflow workflow) {
            super.onStart(report, workflow);
            this.onStartedInvoked = true;
        }

        public boolean isOnCompleteInvoked() {
            return onCompleteInvoked;
        }

        public boolean isOnStartedInvoked() {
            return onStartedInvoked;
        }

        public boolean isOnCancelInvoked() {
            return onCancelInvoked;
        }

        public boolean isOnPendingInvoked() {
            return onPendingInvoked;
        }

        public boolean isOnFailedInvoked() {
            return onFailedInvoked;
        }
    }
}