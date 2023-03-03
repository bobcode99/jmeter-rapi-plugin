package team.sideex.commandlinetool;

import team.sideex.report.ReportGenerator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

//import static team.sideex.report.ReportGenerator.*;

public class Commandline {
    public static void main(String[] args) {
        if(Arrays.asList(args).contains("-help") ||  args.length != 2)
        {
            System.out.println("Proper Usage is: java -jar cmd-tools.jar /path/to/csv true");
            System.out.println("The second args set to false if the csv result file is not useBase64");
            System.exit(0);
        }

        System.out.println("Processing");
        String csvFilePath = args[0];
        boolean useBase64 = Boolean.parseBoolean(args[1]);

        ReportGenerator reportGenerator = new ReportGenerator(useBase64);

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
