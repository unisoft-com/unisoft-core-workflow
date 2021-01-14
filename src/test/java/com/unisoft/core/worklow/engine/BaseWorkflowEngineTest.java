package com.unisoft.core.worklow.engine;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.ConditionalFlow;
import com.unisoft.core.workflow.RepeatFlow;
import com.unisoft.core.workflow.SequentialFlow;
import com.unisoft.core.workflow.Workflow;
import com.unisoft.core.workflow.engine.BaseWorkflowEngineBuilder;
import com.unisoft.core.workflow.engine.WorkflowEngine;
import com.unisoft.core.workflow.work.DefaultExecutionReport;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.Work;
import com.unisoft.core.workflow.work.predicate.EqualPredicate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

import static com.unisoft.core.workflow.work.ExecutionStatus.COMPLETED;
import static com.unisoft.core.workflow.work.ExecutionStatus.FAILED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class BaseWorkflowEngineTest {
    private static final Logger log = LoggerFactory.getLogger(BaseWorkflowEngineTest.class);

    @Test
    void run() {
        final Workflow workflow = mock(Workflow.class);

        final Context context = mock(Context.class);

        final WorkflowEngine engine = BaseWorkflowEngineBuilder.build();

        // when
        engine.run(workflow, context);

        // verify
        Mockito.verify(workflow).execute(context);
    }

    @Test
    void composeComplexWorkflowAndRunIt() {
        String repeatCountKey = "repeat_count";
        String resultKey = "result";
        final RepeatFlow repeatFlow = new RepeatFlow.Builder()
                .name("repeat 3 times")
                .work(context ->
                        context.getData(repeatCountKey, Integer.class)
                                .map(count -> new DefaultExecutionReport(COMPLETED, context.addData(repeatCountKey, count + 1)))
                                .orElse(new DefaultExecutionReport(FAILED, context, new NoSuchElementException(repeatCountKey)))
                )
                .predicate(report -> report.status() == COMPLETED && (report.context().getData(repeatCountKey).isPresent() && report.context().getData(repeatCountKey, Integer.class).get() < 3))
                .build();
        final ConditionalFlow conditionalFlow = new ConditionalFlow.Builder()
                .name("conditional 3 times reached")
                .initially(context -> context.getData(repeatCountKey, Integer.class)
                        .map(value -> {
                            if (value == 3) {
                                return new DefaultExecutionReport(COMPLETED, context);
                            } else {
                                return new DefaultExecutionReport(FAILED, context, new IllegalAccessException("did not reach 3 times"));
                            }
                        })
                        .orElse(new DefaultExecutionReport(FAILED, context, new NoSuchElementException(repeatCountKey))))
                .onFail(context -> new DefaultExecutionReport(FAILED, context.addData(resultKey, "repeat flow failed")))
                .onSuccess(context -> new DefaultExecutionReport(COMPLETED, context.addData(resultKey, "repeat flow success")))
                .predicate(new EqualPredicate(repeatCountKey, 3))
                .build();

        Work finalFlow = context ->
                context.getData(resultKey).map(result -> {
                    log.info("final result: {}", result);
                    return new DefaultExecutionReport(COMPLETED, context);
                }).orElse(new DefaultExecutionReport(FAILED, context));

        final SequentialFlow sequentialFlow = new SequentialFlow.Builder()
                .name("sequential")
                .initially(repeatFlow)
                .then(conditionalFlow)
                .then(finalFlow)
                .build();

        final WorkflowEngine engine = BaseWorkflowEngineBuilder.build();
        final ExecutionReport report = engine.run(sequentialFlow, new Context(repeatCountKey, 0));
        assertEquals(COMPLETED, report.status());
    }
}