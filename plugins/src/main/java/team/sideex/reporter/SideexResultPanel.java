package team.sideex.reporter;

import org.apache.jmeter.gui.util.FilePanel;
import team.sideex.report.ReportGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SideexResultPanel extends JPanel {

    private static final String[] ONLY_ACCEPT = {".jtl", ".csv"};
    private final FilePanel filePanel;
    private ReportGenerator reportGenerator;

    public SideexResultPanel() {
        setLayout(new BorderLayout(0, 5));
        filePanel = new FilePanel("Fill in the csv file path", ONLY_ACCEPT);
        JButton startGenerateReportButton = new JButton("Generate Report");
        filePanel.add(startGenerateReportButton);


        startGenerateReportButton.addActionListener(e -> {
            String csvFilePath = filePanel.getFilename();
            reportGenerator = new ReportGenerator(true);
            try {
                ReportGenerator.checkCsvFileExist(csvFilePath);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            ArrayList<String> sideexReportArrayList = reportGenerator.getSideexReportArrayList(csvFilePath);
            ReportGenerator.generateReport(sideexReportArrayList, csvFilePath);
        });
        add(filePanel, BorderLayout.NORTH);
    }

}
