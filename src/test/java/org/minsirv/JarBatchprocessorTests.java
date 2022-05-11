package org.minsirv;

import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class JarBatchprocessorTests {

    private JarValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = JarValidatorFactory.createJarValidator();
    }

    @Test
    public void filterByGender() throws ParseException, InterruptedException, IOException {
//        male filter
        List<String> args = new ArrayList();
        args.add("-i");
        args.add("in_test.txt");
        args.add("-f");
        args.add("male");
        App.main(args.toArray(new String[0]));

        BufferedReader reader = Files.newBufferedReader(Paths.get("valid.txt"));
        String line;
        while((line=reader.readLine())!=null){
            assertTrue(line.startsWith("3") || line.startsWith("5"));
        }

//        female filter
        args = new ArrayList();
        args.add("-i");
        args.add("in_test.txt");
        args.add("-f");
        args.add("female");
        App.main(args.toArray(new String[0]));

        reader = Files.newBufferedReader(Paths.get("valid.txt"));
        line = "";
        while((line=reader.readLine())!=null){
            assertTrue(line.startsWith("4") || line.startsWith("6"));
        }
    }

    @Test
    public void sortByDateOfBirth() throws IOException, ParseException, InterruptedException {
//        default sort (asc)
        List<String> args = new ArrayList();
        args.add("-i");
        args.add("in_test.txt");
        App.main(args.toArray(new String[0]));

        BufferedReader reader = Files.newBufferedReader(Paths.get("valid.txt"));

        String prior = reader.readLine();
        String current = "";
        while ((current=reader.readLine())!=null) {
            assertTrue(validator.toDate(prior).compareTo(validator.toDate(current)) <= 0);
        }

//        sort desc
        args = new ArrayList();
        args.add("-i");
        args.add("in_test.txt");
        args.add("-sbd");
        args.add("desc");
        App.main(args.toArray(new String[0]));

        reader = Files.newBufferedReader(Paths.get("valid.txt"));

        prior = reader.readLine();
        current = "";
        while ((current=reader.readLine())!=null) {
            assertTrue(validator.toDate(prior).compareTo(validator.toDate(current)) >= 0);
        }

//        sort asc
        args = new ArrayList();
        args.add("-i");
        args.add("in_test.txt");
        args.add("-sbd");
        args.add("asc");
        App.main(args.toArray(new String[0]));

        reader = Files.newBufferedReader(Paths.get("valid.txt"));

        prior = reader.readLine();
        current = "";
        while ((current=reader.readLine())!=null) {
            assertTrue(validator.toDate(prior).compareTo(validator.toDate(current)) <= 0);
        }
    }
}
