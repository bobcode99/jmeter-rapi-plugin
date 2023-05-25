package ncku.selab.rapi4jmeter.reporter;

import ncku.selab.rapi4jmeter.report.ReportGenerator;
import org.apache.jmeter.gui.util.FilePanel;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class RapiResultPanel extends JPanel {

    private static final String[] ONLY_ACCEPT = {".jtl", ".csv"};
    private final FilePanel filePanel;
    private ReportGenerator reportGenerator;

    public RapiResultPanel() {
        setLayout(new BorderLayout(0, 5));
        filePanel = new FilePanel("Fill in the csv file path", ONLY_ACCEPT);
        JButton startGenerateReportButton = new JButton("Generate Report");
        filePanel.add(startGenerateReportButton);


        startGenerateReportButton.addActionListener(e -> {
            String csvFilePath = filePanel.getFilename();
            reportGenerator = new ReportGenerator();
            try {
                ReportGenerator.checkCsvFileExist(csvFilePath);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            ArrayList<String> rapiReportArrayList = reportGenerator.getRapiReportArrayList(csvFilePath);
            ReportGenerator.generateReport(rapiReportArrayList, csvFilePath);
        });
        add(filePanel, BorderLayout.NORTH);
    }

}
