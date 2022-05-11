package org.minsirv.utils;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import java.util.Properties;

public class BatchUtils {

    public static Properties getParameters(JobContext jobCtx) {
        JobOperator operator = BatchRuntime.getJobOperator();
        return operator.getParameters(jobCtx.getExecutionId());
    }
}
