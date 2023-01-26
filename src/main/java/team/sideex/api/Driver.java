/*
 * @link    https://github.com/fourcolor/sideex-api-java
 * @author  fourcolor
 */

package team.sideex.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import team.sideex.api.config.Browser;
import team.sideex.api.config.Config;
import team.sideex.api.config.WebDriverConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Driver {
    private final Config config;
    private final String runnerPath;

    public Driver(String runnerPath, Config config) {
        this.runnerPath = runnerPath;
        this.config = config;
    }

    public static ArrayList<String> getBrowserArgs(String browserName) {
        HashMap<String, ArrayList<String>> browserArgs = new HashMap<>();
        browserArgs.put("chrome", new ArrayList<>(Arrays.asList("headless", "disable-gpu", "window-size=1080,720", "no-sandbox")));
        browserArgs.put("firefox", new ArrayList<>(Arrays.asList("-headless", "-disable-gpu", "-window-size=1080,720")));
        browserArgs.put("MicrosoftEdge", new ArrayList<>(Arrays.asList("headless", "disable-gpu", "window-size=1080,720", "no-sandbox")));

        return browserArgs.get(browserName);
    }

    public static String getBrowserOptions(String browserName) {
        HashMap<String, String> browserOptions = new HashMap<>();
        browserOptions.put("chrome", "goog:chromeOptions");
        browserOptions.put("firefox", "moz:firefoxOptions");
        browserOptions.put("MicrosoftEdge", "ms:edgeOptions");
        return browserOptions.get(browserName);
    }

    public static void main(String[] args) throws Exception {

        ArrayList<String> testSuites = new ArrayList<String>();
        testSuites.add("/Users/user/sideex-things/runner-testcase/suiteOpenWebsite.json");

        Browser browser = new Browser();
        Map<String, Object> caps = new HashMap();

        // browserName: {"chrome", "firefox", "MicrosoftEdge"}
        String browserNameValue = "MicrosoftEdge";
        caps.put("browserName", browserNameValue);
        HashMap<String, ArrayList<String>> browserArgs = new HashMap<>();

        // setBrowserArgs: "args": ["-headless","-disable-gpu", "-window-size=1080,720"]
        browserArgs.put("args", getBrowserArgs(browserNameValue));

        // set browserOptions: "moz:firefoxOptions": {"args": ["-headless","-disable-gpu", "-window-size=1080,720"]}
        caps.put(getBrowserOptions(browserNameValue), browserArgs);

        browser.setCapability(caps);

        System.out.println("browser.getCapability: " + browser.getCapability());

        ArrayList<Browser> browsers = new ArrayList<Browser>();
        browsers.add(browser);

        WebDriverConfig webDriverConfig = new WebDriverConfig();
        webDriverConfig.setBrowsers(browsers);
        webDriverConfig.setServerUrl("http://127.0.0.1:4445");
        ArrayList<WebDriverConfig> webDriverConfigs = new ArrayList<WebDriverConfig>();
        webDriverConfigs.add(webDriverConfig);

        Config config = new Config();
        config.getInput().setTestSuites(testSuites);
        config.getWebdriver().setConfigs(webDriverConfigs);

        Driver driver = new Driver(
                "/Users/user/sideex-things/runner-testcase/runner-executable-file-oneline/sideex-runner-macos-arm64", config);

        JsonNode report = driver.run();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(report));
    }

    // private JsonNode parseReport(InputStream input) throws IOException {
    //     String startToken = "INFO Start to send json report to api";
    //     String endToken = "INFO End of sending json report to api";
    //     BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    //     String line = null;
    //     boolean flag = false;
    //     StringBuilder reportStr = new StringBuilder();
    //     while ((line = reader.readLine()) != null) {
    //         if (line.equals(endToken)) {
    //             flag = false;
    //         }
    //         if (flag) {
    //             reportStr.append(line);
    //         }
    //         if (line.equals(startToken)) {
    //             flag = true;
    //         }
    //     }
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     JsonNode jsonNode = objectMapper.readTree(reportStr.toString());
    //     return jsonNode;
    // }

    public JsonNode run() throws Exception {
        Proc proc = new Proc(runnerPath);
        JsonNode result = proc.run(config.toString());
        // System.out.println(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));

        // if (result.getStatus() != 0) {
        //     System.out.println(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));
        //     throw new Exception(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8));

        // }
        return result;
    }
}
