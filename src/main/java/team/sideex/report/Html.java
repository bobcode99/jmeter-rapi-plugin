/*
 * @author  HSU
 */
package team.sideex.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Html {

    public void generate(String report, String reportPath) throws IOException {


        String html = (report);

        //flexible path
        File file = new File(reportPath + "sideexReport.html");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(html);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        file = null;
    }


}
