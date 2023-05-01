/*
 * @author  HSU
 */
package ncku.selab.rapi4jmeter.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Html {

    public void generate(String report, String reportPath) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd--HH-mm-ss");
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        System.out.println(dtf.format(localDateTimeNow));
        //flexible path
        File file = new File(reportPath + "rapiReport-" + localDateTimeNow + ".html");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write((report));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
