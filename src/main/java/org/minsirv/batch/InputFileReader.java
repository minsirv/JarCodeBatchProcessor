package org.minsirv.batch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minsirv.utils.BatchUtils;

import javax.batch.api.chunk.ItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InputFileReader implements ItemReader {

    private static final Logger logger = LogManager.getLogger(InputFileReader.class);
    private BufferedReader breader;
    @Inject
    JobContext jobCtx;

    @Override
    public void open(Serializable ckpt) throws IOException {
        String fileName = BatchUtils.getParameters(jobCtx).getProperty("inputFile");

        breader = Files.newBufferedReader(Paths.get(fileName));
    }

    @Override
    public void close() throws Exception {
        breader.close();
    }

    @Override
    public String readItem() throws IOException {
        String line = breader.readLine();
        return line;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
