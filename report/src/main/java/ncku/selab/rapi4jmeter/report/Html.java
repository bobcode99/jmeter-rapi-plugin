/*
 * @author  HSU
 */
package ncku.selab.rapi4jmeter.report;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Html {

    public void generate(String reportPath, Map<String, Object> reportDataMap) throws IOException {
        Configuration cfg = TemplateUtil.getTemplateConfig();

        // Get the template (uses cache internally)
        Template template = cfg.getTemplate("report.ftl.html");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        String timeNowReportUse = dtf.format(localDateTimeNow);
        String reportHtmlFileName = "rapiReport-" + timeNowReportUse + ".html";

        // Create the full path for the report file
        String mergedPathString = reportPath + File.separator + reportHtmlFileName;

        // Create the report file
        File file = new File(mergedPathString);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            template.process(reportDataMap, writer);
            writer.close();

            if (Desktop.isDesktopSupported()) {
                // Open the generated report file in the default web browser
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } else {
                System.out.println("Desktop is not supported. Cannot open the file.");
            }

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
