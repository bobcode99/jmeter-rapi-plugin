package team.sideex.reporter;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.jmeter.gui.util.FilePanel;
import org.json.simple.parser.ParseException;
import team.sideex.report.RequestStatsReport;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SideexResultPanel extends JPanel {

    private static final String[] ONLY_ACCEPT = {".jtl", ".csv"};
    private final FilePanel filePanel;
    private RequestStatsReport requestStatsReport;

    private void checkCsvFileExist(String path) throws FileNotFoundException {
        if(!new File(path).isFile()){
            throw new FileNotFoundException("This .csv file not found.");
        }
    }

    private void checkCsvContentIsEmpty(ArrayList<String> arrayList) {
        if(arrayList.size() == 0)  {
            throw new RuntimeException("Please choose a .csv file that have sideex result.");
        }
    }

    private void generateReport(ArrayList<String> arrayList, String csvFilePath) {
        String generateReportPath = csvFilePath.substring(0, csvFilePath.lastIndexOf('/'));

        if (generateReportPath.contains("/"))
            generateReportPath += "/";
        else {
            generateReportPath += "\\";
        }

        try {
            requestStatsReport = new RequestStatsReport();
            requestStatsReport.startGenerateReport(generateReportPath, arrayList);
        } catch (java.text.ParseException | ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public SideexResultPanel() {
        setLayout(new BorderLayout(0, 5));
        filePanel = new FilePanel("Fill in the csv file path", ONLY_ACCEPT);
        JButton startGenerateReportButton = new JButton("Generate Report");
        filePanel.add(startGenerateReportButton);


        startGenerateReportButton.addActionListener(e -> {
            String csvFilePath = filePanel.getFilename();

            try {
                checkCsvFileExist(csvFilePath);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            ArrayList<String> sideexReportArrayList = getSideexReportArrayList(csvFilePath);
            generateReport(sideexReportArrayList, csvFilePath);
        });
        add(filePanel, BorderLayout.NORTH);
    }

    private ArrayList<String> getSideexReportArrayList(String path) {
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
