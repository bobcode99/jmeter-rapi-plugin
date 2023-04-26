package team.sideex.report;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import com.opencsv.exceptions.CsvException;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {

    public static void generateReport(ArrayList<String> reportArrayList, String csvFilePath) {
        String generateReportPath = csvFilePath.substring(0, csvFilePath.lastIndexOf('/'));

        if (generateReportPath.contains("/"))
            generateReportPath += "/";
        else {
            generateReportPath += "\\";
        }

        try {
            RequestStatsReport requestStatsReport = new RequestStatsReport();
            requestStatsReport.startGenerateReport(generateReportPath, reportArrayList);
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

    public ArrayList<String> getSideexReportArrayList(String path) {
        ArrayList<String> sideexReportArrayList = new ArrayList<>();

        RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().build();
        try {

            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(new FileReader(path));
            csvReaderBuilder.withCSVParser(rfc4180Parser);
            CSVReader csvReader = csvReaderBuilder.build();

            List<String[]> records = csvReader.readAll();

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
