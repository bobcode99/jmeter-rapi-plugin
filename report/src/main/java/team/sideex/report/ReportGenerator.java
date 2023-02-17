package team.sideex.report;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    public static void generateReport(ArrayList<String> arrayList, String csvFilePath) {
        String generateReportPath = csvFilePath.substring(0, csvFilePath.lastIndexOf('/'));

        if (generateReportPath.contains("/"))
            generateReportPath += "/";
        else {
            generateReportPath += "\\";
        }

        try {
            RequestStatsReport requestStatsReport = new RequestStatsReport();
            requestStatsReport.startGenerateReport(generateReportPath, arrayList);
        } catch (java.text.ParseException | ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void checkCsvContentIsEmpty(ArrayList<String> arrayList) {
        if (arrayList.size() == 0) {
            throw new RuntimeException("Please choose a .csv file that have sideex result.");
        }
    }

    public static void checkCsvFileExist(String path) throws FileNotFoundException {
        if (!new File(path).isFile()) {
            throw new FileNotFoundException("This .csv file not found.");
        }
    }

    public static ArrayList<String> getSideexReportArrayList(String path) {
        ArrayList<String> sideexReportArrayList = new ArrayList<>();

        try {
            CSVReader reader = new CSVReader(new FileReader(path));

            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                // process each record
                Character firstCharacter = record[4].charAt(0);
                if (firstCharacter.equals('{')) {
                    sideexReportArrayList.add(record[4]);
                }
            }

            checkCsvContentIsEmpty(sideexReportArrayList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        return sideexReportArrayList;
    }
}
