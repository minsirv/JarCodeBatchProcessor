package org.minsirv.batch;

import javax.batch.api.chunk.ItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.List;

public class OutputFileWriter implements ItemWriter {

    private BufferedWriter validWriter;
    private BufferedWriter invalidWriter;
    @Inject
    JobContext jobCtx;


    @Override
    public void open(Serializable ckpt) throws Exception {
        String validFileName = jobCtx.getProperties().getProperty("valid_file");
        String invalidFileName = jobCtx.getProperties().getProperty("invalid_file");
        validWriter = new BufferedWriter(new FileWriter(validFileName, false));
        invalidWriter = new BufferedWriter(new FileWriter(invalidFileName, false));
    }

    @Override
    public void close() throws Exception {
        validWriter.close();
        invalidWriter.close();
    }

    @Override
    public void writeItems(List<Object> list) throws Exception {
        for (Object item : list) {
            String line = (String) item;
            if (line.endsWith(":true")) {
                validWriter.write(line);
                validWriter.newLine();
            } else {
                invalidWriter.write(line);
                invalidWriter.newLine();
            }
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
