package ncku.selab.rapi.api;

import ncku.selab.rapi.api.config.Config;

import java.io.IOException;

public class Rapi {
    private final Config config;
    private final String runnerPath;

    /**
     * 
     * @param runnerPath The rapi runner file path
     * @param config     The config about input, report, play, webdricer setting
     */
    public Rapi(String runnerPath, Config config) {
        this.runnerPath = runnerPath;
        this.config = config;
    }

    /**
     * The function will run the test depend on the config you set, after finish the
     * test it will pass the report back
     * 
     * @return RapiReport
     * @throws IOException
     * @throws InterruptedException
     * @throws Exception
     */
    public RapiReport run() throws IOException, InterruptedException, Exception {
        Proc proc = new Proc(runnerPath);
        ProcResult result = proc.run(config.toString());
        System.err.println(result.getStderr());
        return new RapiReport(result.getStdout(), config.getReport().getType());
    }
}
