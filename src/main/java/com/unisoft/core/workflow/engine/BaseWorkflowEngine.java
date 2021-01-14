package com.unisoft.core.workflow.engine;

import com.unisoft.core.util.Context;
import com.unisoft.core.workflow.Workflow;
import com.unisoft.core.workflow.work.ExecutionReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * base workflow engine.
 *
 * @author omar.H.Ajmi
 * @since 30/10/2020
 */
public class BaseWorkflowEngine implements WorkflowEngine {

    private static final Logger log = LoggerFactory.getLogger(BaseWorkflowEngine.class);

    /**
     * {@inheritDoc}
     */
    public ExecutionReport run(Workflow work, Context context) {
        log.info("running work {}", work.name());
        return work.execute(context);
    }
}
