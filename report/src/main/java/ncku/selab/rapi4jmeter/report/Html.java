/*
 * @author  HSU
 */
package ncku.selab.rapi4jmeter.report;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TimeZone;

public class Html {

    public void generate(String reportPath, Map<String, Object> reportDataMap) throws IOException, TemplateException {

        /* ------------------------------------------------------------------------ */
        /* You should do this ONLY ONCE in the whole application life-cycle:        */

        /* Create and adjust the configuration singleton */
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassForTemplateLoading(ReportGenerator.class, "/template");

        // Recommended settings for new projects:
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
        /* ------------------------------------------------------------------------ */

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
