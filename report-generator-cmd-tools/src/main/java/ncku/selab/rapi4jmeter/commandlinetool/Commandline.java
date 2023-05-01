package ncku.selab.rapi4jmeter.commandlinetool;

import ncku.selab.rapi4jmeter.report.ReportGenerator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class Commandline {
    public static void main(String[] args) {
        if(Arrays.asList(args).contains("-help") ||  args.length != 1)
        {
            System.out.println("Proper Usage is: java -jar cmd-tools.jar /path/to/csv");
            System.exit(0);
        }

        System.out.println("Processing");
        String csvFilePath = args[0];

        ReportGenerator reportGenerator = new ReportGenerator();

        try {
            ReportGenerator.checkCsvFileExist(csvFilePath);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        ArrayList<String> rapiReportArrayList = reportGenerator.getRapiReportArrayList(csvFilePath);
        ReportGenerator.generateReport(rapiReportArrayList, csvFilePath);
        System.out.println("Finish generating report.");
    }
}
