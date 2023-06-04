package ncku.selab.rapi4jmeter.report;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.util.TimeZone;

public class TemplateUtil {
    private static final Configuration templateConfiguration = init();

    private static Configuration init() {
        /* ------------------------------------------------------------------------ */
        /* You should do this ONLY ONCE in the whole application life-cycle:        */

        /* Create and adjust the configuration singleton */
        // Use VERSION_2_3_31 because JMeter 5.5 using freemarker-2.3.31.jar
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(ReportGenerator.class, "/template");

        // Recommended settings for new projects:
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
        /* ------------------------------------------------------------------------ */
        return cfg;
    }

    public static Configuration getTemplateConfig() {
        return templateConfiguration;
    }
}
