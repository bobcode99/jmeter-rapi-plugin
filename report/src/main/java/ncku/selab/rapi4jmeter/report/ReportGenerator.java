package ncku.selab.rapi4jmeter.report;

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
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {

    public static void generateReport(ArrayList<String> reportArrayList, String csvFilePath) {
        Path path = FileSystems.getDefault().getPath(csvFilePath);
        Path directoryPath = path.getParent();
        String directory = "";
        if (directoryPath != null) {
            directory = directoryPath.toString();
            System.out.println("Generate report directory path: " + directory);
        } else {
            throw new Error("Invalid file path or the file is in the root directory.");
        }

        try {
            RequestStatsReport requestStatsReport = new RequestStatsReport();
            requestStatsReport.startGenerateReport(directory, reportArrayList);
        } catch (java.text.ParseException | ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void checkCsvContentIsEmpty(ArrayList<String> arrayList) {
        if (arrayList.size() == 0) {
            throw new RuntimeException("Please choose a .csv file that have Rapi result.");
        }
    }

    public static void checkCsvFileExist(String path) throws FileNotFoundException {
        if (!new File(path).isFile()) {
            throw new FileNotFoundException("This .csv file not found.");
        }
    }

    public ArrayList<String> getRapiReportArrayList(String path) {
        ArrayList<String> rapiReportArrayList = new ArrayList<>();

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
                    rapiReportArrayList.add(record[4]);
                }
            }
            checkCsvContentIsEmpty(rapiReportArrayList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        return rapiReportArrayList;
    }
}
