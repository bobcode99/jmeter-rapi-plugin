/*
 * @link    https://github.com/RapiTest/rapi-api
 * @author  fourcolor
 */

package ncku.selab.rapi.api;

import com.fasterxml.jackson.databind.JsonNode;
import ncku.selab.rapi.api.config.Config;

import java.util.HashMap;

public class Driver {
    private final Config config;
    private final String runnerPath;

    public Driver(String runnerPath, Config config) {
        this.runnerPath = runnerPath;
        this.config = config;
    }



    public static String getBrowserOptions(String browserName) {
        HashMap<String, String> browserOptions = new HashMap<>();
        browserOptions.put("chrome", "goog:chromeOptions");
        browserOptions.put("firefox", "moz:firefoxOptions");
        browserOptions.put("MicrosoftEdge", "ms:edgeOptions");
        return browserOptions.get(browserName);
    }

    public JsonNode run() throws Exception {
        Proc proc = new Proc(runnerPath);
        // System.out.println(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));

        // if (result.getStatus() != 0) {
        //     System.out.println(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));
        //     throw new Exception(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));

        // }
        return proc.run(config.toString());
    }
}
