/*
 * @author  HSU
 */
package ncku.selab.rapi4jmeter.report;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.awt.Desktop;
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

        /* Get the template (uses cache internally) */
        Template template = cfg.getTemplate("report.ftl.html");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
        LocalDateTime localDateTimeNow = LocalDateTime.now();

        String timeNowReportUse = dtf.format(localDateTimeNow);

        String reportHtmlFileName = "rapiReport-" + timeNowReportUse + ".html";

        //flexible path
        File file = new File(reportPath + reportHtmlFileName);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            template.process(reportDataMap, writer);
            writer.close();

            if (!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
            {
                System.out.println("not supported open file");
                return;
            }
            Desktop desktop = Desktop.getDesktop();

            //checks file exists or not
            if (file.exists()) {
                desktop.open(file);              //opens the specified file
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
