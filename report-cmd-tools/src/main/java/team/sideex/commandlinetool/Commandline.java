package team.sideex.commandlinetool;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static team.sideex.report.ReportGenerator.*;

public class Commandline {
    public static void main(String[] args) {
        System.out.println("hii there");
        String csvFilePath = args[0];
        try {
            checkCsvFileExist(csvFilePath);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        ArrayList<String> sideexReportArrayList = getSideexReportArrayList(csvFilePath);

        generateReport(sideexReportArrayList, csvFilePath);
    }
}
