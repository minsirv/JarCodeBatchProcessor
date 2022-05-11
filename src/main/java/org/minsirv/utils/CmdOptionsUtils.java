package org.minsirv.utils;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CmdOptionsUtils {

    public CommandLine parseOptions(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("f", "filter", true, "filter by gender: male|female");
        options.addOption("sbd", "sort-by-birth-date", true, "sort by date of birth: asc|desc");
        options.addOption("i", "input", true, "path to input file");

        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

}
