package team.sideex.commandlinetool;

import team.sideex.report.ReportGenerator;

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
        ArrayList<String> sideexReportArrayList = reportGenerator.getSideexReportArrayList(csvFilePath);
        ReportGenerator.generateReport(sideexReportArrayList, csvFilePath);
        System.out.println("Finish generating report.");
    }
}
