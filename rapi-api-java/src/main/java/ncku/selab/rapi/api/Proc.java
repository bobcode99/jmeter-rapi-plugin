package ncku.selab.rapi.api;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Proc {
    private String executable;

    public Proc(String executable) {
        this.executable = executable;
    }

    public ProcResult run(String input) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(this.executable, "--call-by-api");
        Process proc = pb.start();
        OutputStream stdin = proc.getOutputStream();
        InputStream stdout = proc.getInputStream();
        InputStream stderr = proc.getErrorStream();
        stdin.write(input.getBytes(StandardCharsets.UTF_8));
        stdin.flush();
        stdin.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stderr));
        StringBuilder stderrStrBuilder = new StringBuilder();
        StringBuilder stdoutStrBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null)
            stderrStrBuilder.append(line);
        reader = new BufferedReader(new InputStreamReader(stdout));
        while ((line = reader.readLine()) != null)
            stdoutStrBuilder.append(line);
        int status = proc.waitFor();
        status = proc.exitValue();
        return new ProcResult(stdoutStrBuilder.toString(), stderrStrBuilder.toString(), status);
    }
}

class ProcResult {
    private final String stdout;
    private final String stderr;
    private final int status;

    ProcResult(String stdout, String stderr, int status) {
        this.status = status;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public int getStatus() {
        return this.status;
    }

    public String getStdout() {
        return this.stdout;
    }

    public String getStderr() {
        return this.stderr;
    }
}
