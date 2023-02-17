package team.sideex.commandlinetool;


import java.io.FileNotFoundException;
import java.util.ArrayList;

import static team.sideex.report.ReportGenerator.*;

public class Commandline {
    public static void main(String[] args) {
        System.out.println("hii there");
        String csvFilePath = "/Users/boby99/distributed-sideex-jmeter-project/sideexsampler-result/result_20230117_133910_no_tc.csv";
        try {
            checkCsvFileExist(csvFilePath);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        ArrayList<String> sideexReportArrayList = getSideexReportArrayList(csvFilePath);

        generateReport(sideexReportArrayList, csvFilePath);
    }
}