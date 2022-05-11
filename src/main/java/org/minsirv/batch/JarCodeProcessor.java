package org.minsirv.batch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minsirv.JarValidator;
import org.minsirv.JarValidatorFactory;
import org.minsirv.utils.BatchUtils;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

public class JarCodeProcessor implements ItemProcessor {
    private final Logger logger = LogManager.getLogger(JarCodeProcessor.class);
    @Inject
    JobContext jobCtx;

    private JarValidator validator;

    public JarCodeProcessor() {
        validator = JarValidatorFactory.createJarValidator();
    }

    @Override
    public Object processItem(Object o) {
        String line = (String) o;
//        filter by gender
        String filter = BatchUtils.getParameters(jobCtx).getProperty("filter");
        if(filter.equalsIgnoreCase("male") && !validator.isMale(line)
                || filter.equalsIgnoreCase("female") && !validator.isFemale(line)) {
            return null;
        }
        line = line + ":" + validator.validate(line);
        return line;
    }

}
