package team.sideex.reporter;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.jmeter.gui.util.FilePanel;
import org.json.simple.parser.ParseException;
import team.sideex.report.RequestStatsReport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SideexResultPanel extends JPanel {

    private JButton startGenerateReportButton;
    private RequestStatsReport requestStatsReport;

    private static final String[] EXTS = { ".xml", ".jtl", ".csv" }; // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$

    private final FilePanel filePanel;

    private ArrayList<String> getSideexReportArrayList(String path) {
        ArrayList<String> sideexReportArrayList = new ArrayList<>();

        try {
            CSVReader reader = new CSVReader(new FileReader(path));

            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                // process each record
                Character firstCharacter = record[4].charAt(0);
//                System.out.println(firstCharacter);
                if (firstCharacter.equals('{')) {
                    sideexReportArrayList.add(record[4]);
                }
            }
        } catch (IOException e) {
            System.out.println("catch getSideexReportArrayList IOException e");
            e.printStackTrace();
        } catch (CsvException e) {
            System.out.println("catch getSideexReportArrayList CsvException e");

            throw new RuntimeException(e);
        }
        return sideexReportArrayList;
    }


    public SideexResultPanel() {
        setLayout(new BorderLayout(0, 5));
        filePanel = new FilePanel("Read from file", EXTS); // $NON-NLS-1$
        startGenerateReportButton = new JButton("Generate Report");
        filePanel.add(startGenerateReportButton);


        requestStatsReport = new RequestStatsReport();
        startGenerateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("click");
                String csvFilePath = filePanel.getFilename();
                ArrayList<String> sideexReportArrayList = getSideexReportArrayList(csvFilePath);

//                System.out.println("sideexReportArrayList: " + sideexReportArrayList);
                String generateReportPath = csvFilePath.substring(0, csvFilePath.lastIndexOf('/') + 1);
                System.out.println("generateReportPath: " + generateReportPath);
                try {
                    requestStatsReport.startGenerateReport(generateReportPath, sideexReportArrayList);
                } catch (IOException ex) {
                    System.out.println("catch IOException ex");
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    System.out.println("catch arseException ex");

                    throw new RuntimeException(ex);
                } catch (java.text.ParseException ex) {
                    System.out.println("catch java.text.ParseException ex");
                    throw new RuntimeException(ex);
                }

            }
        });
        add(filePanel, BorderLayout.NORTH);
    }

}
