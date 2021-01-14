package com.unisoft.core.workflow;

import com.unisoft.core.util.Context;
import com.unisoft.core.util.log.LogUtil;
import com.unisoft.core.workflow.exception.WorkflowException;
import com.unisoft.core.workflow.work.ExecutionReport;
import com.unisoft.core.workflow.work.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * parallel workflow runner, runs a parallel workflow work units in a parallel fashion
 *
 * @author omar.H.Ajmi
 * @since 29/10/2020
 */
class ParallelFlowRunner {
    private static final Logger log = LoggerFactory.getLogger(ParallelFlowRunner.class);
    private final ExecutorService executorService;

    ParallelFlowRunner(ExecutorService executorService) {
        this.executorService = Objects.requireNonNull(executorService, "'executor-service' cannot be null");
    }

    /**
     * parallel work units execution
     *
     * @param units   units to run
     * @param context units context
     * @return list of units reports
     */
    List<ExecutionReport> run(List<Work> units, Context context) {
//        prep tasks for parallel submission
        List<Callable<ExecutionReport>> tasks = new ArrayList<>(units.size());
        units.forEach(work -> tasks.add(() -> work.execute(context)));

//        submit work units and wait for reports
        List<Future<ExecutionReport>> futures;

        try {
            futures = this.executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new WorkflowException("parallel flow was interrupted while executing work units", e);
        }

        final HashMap<Work, Future<ExecutionReport>> futureWorkReports = new HashMap<>();
        for (int index = 0; index < units.size(); index++) {
            futureWorkReports.put(units.get(index), futures.get(index));
        }

//        gather reports
        final List<ExecutionReport> reports = new ArrayList<>();
        for (Map.Entry<Work, Future<ExecutionReport>> entry : futureWorkReports.entrySet()) {
            try {
                reports.add(entry.getValue().get());
            } catch (InterruptedException e) {
                LogUtil.logExceptionAsError(log, () -> {
                    this.shutdownExecutorService();
                    throw new WorkflowException("parallel flow was interrupted while waiting for the result of work unit " + entry.getKey().id());
                });
            } catch (ExecutionException e) {
                LogUtil.logExceptionAsError(log, () -> {
                    this.shutdownExecutorService();
                    throw new WorkflowException("Unable to execute work unit " + entry.getKey().id());
                });
            }
        }
        this.shutdownExecutorService();
        return reports;
    }

    /**
     * shutdown the executor service
     * <p>
     * the ExecutorService will first stop taking new tasks,
     * then wait up to a specified period of time for all tasks to be completed.
     * If that time expires, the execution is stopped immediately.
     * </p>
     */
    private void shutdownExecutorService() {
        this.executorService.shutdown();
        try {
            if (this.executorService.awaitTermination(400, TimeUnit.MILLISECONDS)) {
                this.executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.executorService.shutdownNow();
        }
    }

}
