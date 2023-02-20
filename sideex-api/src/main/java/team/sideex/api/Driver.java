/*
 * @link    https://github.com/fourcolor/sideex-api-java
 * @author  fourcolor
 */

package team.sideex.api;

import com.fasterxml.jackson.databind.JsonNode;
import team.sideex.api.config.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Driver {
    private final Config config;
    private final String runnerPath;

    public Driver(String runnerPath, Config config) {
        this.runnerPath = runnerPath;
        this.config = config;
    }

    public static ArrayList<String> getBrowserArgs(String browserName, boolean enableDevShmUsage) {
        ArrayList<String> chromeArgsList = new ArrayList<>(Arrays.asList("headless", "disable-gpu", "window-size=1080,720", "no-sandbox"));
        ArrayList<String> firefoxArgsList = new ArrayList<>(Arrays.asList("-headless", "-disable-gpu", "-window-size=1080,720"));
        ArrayList<String> msEdgeArgsList = new ArrayList<>(Arrays.asList("headless", "disable-gpu", "window-size=1080,720", "no-sandbox"));

        if(enableDevShmUsage) {
            chromeArgsList.add("--disable-dev-shm-usage");
            msEdgeArgsList.add("--disable-dev-shm-usage");
        }

        HashMap<String, ArrayList<String>> browserArgs = new HashMap<>();
        browserArgs.put("chrome", chromeArgsList);
        browserArgs.put("firefox", firefoxArgsList);
        browserArgs.put("MicrosoftEdge", msEdgeArgsList);

        return browserArgs.get(browserName);
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
