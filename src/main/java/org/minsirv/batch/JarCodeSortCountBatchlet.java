package org.minsirv.batch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minsirv.JarValidator;
import org.minsirv.JarValidatorFactory;
import org.minsirv.utils.BatchUtils;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class JarCodeSortCountBatchlet extends AbstractBatchlet {
    private final Logger logger = LogManager.getLogger(JarCodeSortCountBatchlet.class);

    @Inject
    JobContext jobCtx;

    private JarValidator validator;

    public JarCodeSortCountBatchlet() {
        validator = JarValidatorFactory.createJarValidator();
    }

    @Override
    public String process() throws Exception {
        String validFileName = jobCtx.getProperties().getProperty("valid_file");
        String inputFileName = BatchUtils.getParameters(jobCtx).getProperty("inputFile");

        Long totalLines = Files.lines(Paths.get(inputFileName)).count();
        Long totalValidLines = Files.lines(Paths.get(validFileName)).count();

        FileReader fr = new FileReader(validFileName);
        BufferedReader reader = new BufferedReader(fr);

        ArrayList<String> str = new ArrayList<>();
        String line = "";

        while((line=reader.readLine())!=null){
            str.add(line);
        }
        reader.close();

        Comparator<String> comparator = Comparator.comparing(p -> validator.toDate(p));

        if (BatchUtils.getParameters(jobCtx).getProperty("sortOrder").equalsIgnoreCase("desc")) {
            Collections.sort(str, comparator.reversed());
        } else {
            Collections.sort(str, comparator);
        }

        FileWriter writer = new FileWriter(validFileName);
        for(String s: str){
            writer.write(s);
            writer.write("\r\n");
        }
        writer.close();

        logger.info("Valid ids: " + totalValidLines + " out of " + totalLines);

        return null;
    }

}
