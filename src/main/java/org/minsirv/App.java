package org.minsirv;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minsirv.utils.CmdOptionsUtils;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args ) throws InterruptedException, ParseException, FileNotFoundException {
        CmdOptionsUtils utils = new CmdOptionsUtils();
        CommandLine cmd = utils.parseOptions(args);

        Properties props = new Properties();
        props.setProperty("filter", cmd.getOptionValue("f", ""));
        props.setProperty("sortOrder", cmd.getOptionValue("sbd", ""));
        props.setProperty("inputFile", cmd.getOptionValue("i", ""));

        if (props.getProperty("inputFile").isEmpty()) {
            logger.info("Input file is not defined! use -i flag followed by file path");
            return;
        }

        if (!Files.exists(Paths.get(props.getProperty("inputFile")))) {
            logger.error("Specified file does not exist");
            throw new FileNotFoundException();
        }

        JobOperator jobOperator = BatchRuntime.getJobOperator();

        Instant executionStart = Instant.now();
        logger.info("starting job " + executionStart);

        long execID = jobOperator.start("jarProcessor", props);
        JobExecution execution = jobOperator.getJobExecution(execID);
        while(execution.getBatchStatus() == BatchStatus.STARTED || execution.getBatchStatus() == BatchStatus.STARTING) {
            Thread.sleep(500);
        }

        Instant executionEnd = Instant.now();
        logger.info("jobs done. Duration: " +ChronoUnit.SECONDS.between(executionStart, executionEnd) + " seconds");
    }
}
