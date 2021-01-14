package com.unisoft.core.worklow.work;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.work.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Disabled("funky on build, does not fail when running as standalone test")
class MultiOptionWorkTest {

    @Test
    void build() {
        Work work = context -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context);
        final MultiOptionWork.Builder builder = new MultiOptionWork.Builder();
        assertThrows(NullPointerException.class, builder::build);
        builder.action(work)
                .onComplete(work);

        assertDoesNotThrow(builder::build);

        builder.onCancel(work);
        assertDoesNotThrow(builder::build);

        builder.onFail(work);
        assertDoesNotThrow(builder::build);

        builder.onSuspend(work);
        assertDoesNotThrow(builder::build);

    }

    @Test
    void executeWithNoOptions() {
        final MultiOptionWork.Builder builder = new MultiOptionWork.Builder();
        builder.action(context -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context));

        final MultiOptionWork multiOptionWork = assertDoesNotThrow(builder::build);
        assertNotNull(multiOptionWork);
        final ExecutionReport report = multiOptionWork.execute(new Context("key", "value"));
        assertEquals(ExecutionStatus.COMPLETED, report.status());
    }

    @Test
    void executeWithCompleteOption() {
        Work completeWork = mock(Work.class);
        final Context contxt = new Context("key", "value");
        final MultiOptionWork.Builder builder = new MultiOptionWork.Builder();
        builder.action(context -> new DefaultExecutionReport(ExecutionStatus.COMPLETED, context))
                .onComplete(completeWork);

        final MultiOptionWork optionWork = builder.build();

        optionWork.execute(contxt);
        Mockito.verify(completeWork).execute(contxt);
    }

    @Test
    void executeWithFailOption() {
        Work failWork = mock(Work.class);
        final Context contxt = new Context("key", "value");
        final MultiOptionWork.Builder builder = new MultiOptionWork.Builder();
        builder.action(context -> new DefaultExecutionReport(ExecutionStatus.FAILED, context))
                .onFail(failWork);

        final MultiOptionWork optionWork = builder.build();

        optionWork.execute(contxt);
        Mockito.verify(failWork).execute(contxt);
    }

    @Test
    void executeWithCancelOption() {
        Work cancelWork = mock(Work.class);
        final Context contxt = new Context("key", "value");
        final MultiOptionWork.Builder builder = new MultiOptionWork.Builder();
        builder.action(context -> new DefaultExecutionReport(ExecutionStatus.CANCELED, context))
                .onCancel(cancelWork);

        final MultiOptionWork optionWork = builder.build();

        optionWork.execute(contxt);
        Mockito.verify(cancelWork).execute(contxt);
    }

    @Test
    void executeWithSuspendOption() {
        Work suspend = mock(Work.class);
        final Context contxt = new Context("key", "value");
        final MultiOptionWork.Builder builder = new MultiOptionWork.Builder();
        builder.action(context -> new DefaultExecutionReport(ExecutionStatus.PENDING, context))
                .onSuspend(suspend);

        final MultiOptionWork optionWork = builder.build();

        optionWork.execute(contxt);
        Mockito.verify(suspend).execute(contxt);
    }
}