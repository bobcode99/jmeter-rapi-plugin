/*
 * @link    https://github.com/RapiTest/rapi-api
 * @author  fourcolor
 */
package ncku.selab.rapi.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Proc {
    private final String executable;

    public Proc(String executable) {
        this.executable = executable;
    }

    public JsonNode run(String input) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(this.executable, "--call-by-api");
        Process proc = pb.start();
        OutputStream stdin = proc.getOutputStream();
        InputStream stdout = proc.getInputStream();
        InputStream stderr = proc.getErrorStream();
        stdin.write(input.getBytes(StandardCharsets.UTF_8));
        stdin.flush();
        stdin.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stderr));
        JsonNode report = parseReport(stdout);
        String stderrStr;
        String line = null;
        while ((line = reader.readLine()) != null)
            System.out.println(line);
        int status = proc.waitFor();
        return report;
    }

    private JsonNode parseReport(InputStream input) throws IOException {
        String startToken = "INFO Start to send json report to api";
        String endToken = "INFO End of sending json report to api";
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = null;
        boolean flag = false;
        StringBuilder reportStr = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            if (line.equals(endToken)) {
                flag = false;
            }
            if (flag) {
                reportStr.append(line);
            }
            if (line.equals(startToken)) {
                flag = true;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(reportStr.toString());
    }
}

class ProcResult {
    private final InputStream stdout;
    // private InputStream stderr;
    private final int status;

    ProcResult(InputStream stdout, InputStream stderr, int status) {
        this.status = status;
        this.stdout = stdout;
        // this.stderr = stderr;
    }

    public int getStatus() {
        return this.status;
    }

    public InputStream getStdout() {
        return this.stdout;
    }

    // public InputStream getStderr() {
    //     return this.stderr;
    // }
}
