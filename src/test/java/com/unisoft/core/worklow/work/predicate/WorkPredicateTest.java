package com.unisoft.core.worklow.work.predicate;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.work.DefaultExecutionReport;
import com.unisoft.core.workflow.work.ExecutionStatus;
import com.unisoft.core.workflow.work.predicate.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.unisoft.core.workflow.work.ExecutionStatus.COMPLETED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkPredicateTest {

    private static Stream<Arguments> equalPredicateSupplier() {
        final Context context = new Context("key", "value");
        return Stream.of(Arguments.of("", ""),
                Arguments.of(Integer.parseInt("1"), Integer.parseInt("1")),
                Arguments.of(context, context),
                Arguments.of(Boolean.TRUE, Boolean.TRUE));
    }

    private static Stream<Arguments> notEqualPredicateSupplier() {
        final Context context = new Context("key", "value");
        return Stream.of(Arguments.of("", "foo"),
                Arguments.of(Integer.parseInt("1"), Integer.parseInt("2")),
                Arguments.of(context, new Context("key", "value")),
                Arguments.of(Boolean.TRUE, Boolean.FALSE));
    }

    @Test
    void apply() {
        WorkPredicate predicate = report -> report.status() == ExecutionStatus.COMPLETED;
        assertTrue(predicate.apply(new DefaultExecutionReport(ExecutionStatus.COMPLETED, new Context("key", "value"))));
        assertFalse(predicate.apply(new DefaultExecutionReport(ExecutionStatus.FAILED, new Context("key", "value"))));
    }

    @Test
    void integerLessThen() {
        IntegerLessThan predicate = new IntegerLessThan("key", 30);

        assertTrue(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 10))));
        assertFalse(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 100))));
    }

    @Test
    void integerGreaterThen() {
        IntegerGreaterThan predicate = new IntegerGreaterThan("key", 30);

        assertTrue(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 100))));
        assertFalse(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 10))));
    }

    @Test
    void doubleLessThen() {
        DoubleLessThan predicate = new DoubleLessThan("key", 30.00);

        assertTrue(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 10D))));
        assertFalse(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 100D))));
    }

    @Test
    void doubleGreaterThen() {
        DoubleGreaterThan predicate = new DoubleGreaterThan("key", 30D);

        assertTrue(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 100D))));
        assertFalse(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 10D))));
    }

    @Test
    void floatLessThen() {
        FloatLessThan predicate = new FloatLessThan("key", 30.0F);

        assertTrue(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 10F))));
        assertFalse(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 100F))));
    }

    @Test
    void floatGreaterThen() {
        FloatGreaterThan predicate = new FloatGreaterThan("key", 30.0F);

        assertTrue(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 100F))));
        assertFalse(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", 10F))));
    }

    @Test
    void bigDecimalLessThen() {
        BigDecimalLessThan predicate = new BigDecimalLessThan("key", new BigDecimal(30));

        assertTrue(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", new BigDecimal(10)))));
        assertFalse(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", new BigDecimal(100)))));
    }

    @Test
    void bigDecimalGreaterThen() {
        BigDecimalGreaterThan predicate = new BigDecimalGreaterThan("key", new BigDecimal(30));

        assertTrue(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", new BigDecimal(100)))));
        assertFalse(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", new BigDecimal(10)))));
    }

    @ParameterizedTest
    @MethodSource("equalPredicateSupplier")
    void equalPredicate(Object actualValue, Object expectedValue) {
        final EqualPredicate predicate = new EqualPredicate("key", expectedValue);
        assertTrue(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", actualValue))));
    }

    @ParameterizedTest
    @MethodSource("notEqualPredicateSupplier")
    void notEqualPredicate(Object actualValue, Object expectedValue) {
        final EqualPredicate predicate = new EqualPredicate("key", expectedValue);
        assertFalse(predicate.apply(new DefaultExecutionReport(COMPLETED, new Context("key", actualValue))));
    }
}