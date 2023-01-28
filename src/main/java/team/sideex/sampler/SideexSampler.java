package team.sideex.sampler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.logging.log4j.util.Strings;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.sideex.api.Driver;
import team.sideex.api.config.Browser;
import team.sideex.api.config.Config;
import team.sideex.api.config.WebDriverConfig;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static team.sideex.api.Driver.getBrowserArgs;
import static team.sideex.api.Driver.getBrowserOptions;

public class SideexSampler extends AbstractSampler {

    private static final Logger LOG = LoggerFactory.getLogger(SideexSampler.class);

    private static final String LABEL = "LABEL";
    private static final String RESPONSE_CODE = "RESPONSE_CODE";
    private static final String SUCCESS = "SUCCESS";
    private static final String RESPONSE_MESSAGE = "RESPONSE_MESSAGE";
    private static final String TC_FILE_PATH = "TC_FILE_PATH";

    private static final String BROWSER_SELECT = "BROWSER_SELECT";

    public void setLabel(String label) {
        setProperty(LABEL, label);
    }

    public String getLabel() {
        return getPropertyAsString(LABEL, Strings.EMPTY);
    }

    public void setResponseCode(String responseCode) {
        setProperty(RESPONSE_CODE, responseCode);
    }

    public String getResponseCode() {
        return getPropertyAsString(RESPONSE_CODE, "200");
    }

    public void setSuccessful(boolean success) {
        setProperty(SUCCESS, success);
    }

    public boolean getSuccessful() {
        return getPropertyAsBoolean(SUCCESS, true);
    }

    public void setResponseMessage(String responseMessage) {
        setProperty(RESPONSE_MESSAGE, responseMessage);
    }

    public String getResponseMessage() {
        return getPropertyAsString(RESPONSE_MESSAGE, "success message");
    }

    public void setTestCaseFilePath(String filePath) {
        setProperty(TC_FILE_PATH, filePath);
    }

    public String getTestCaseFilePath() {
        return getPropertyAsString(TC_FILE_PATH, "");
    }

    public void setBrowserSelect(String browserName) {
        setProperty(BROWSER_SELECT, browserName);
    }

    public String getBrowserSelect() {
        return getPropertyAsString(BROWSER_SELECT, "Firefox");
    }

    class ResultSideex {
        private String jsonReport;
        private boolean successfulStatus;
    }

    private ResultSideex startRunSideex(String testCase, String browserName) throws Exception {
        ArrayList<String> testSuites = new ArrayList<String>();
        testSuites.add(testCase);

        Browser browser = new Browser();
        Map<String, Object> caps = new HashMap();

        // browserName: {"chrome", "firefox", "MicrosoftEdge"}
        String browserNameValue = browserName;
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

        // TODO: write jmeter plugin config to send executable sideex runner's path and selenium port
        webDriverConfig.setServerUrl("http://127.0.0.1:4445");
        ArrayList<WebDriverConfig> webDriverConfigs = new ArrayList<WebDriverConfig>();
        webDriverConfigs.add(webDriverConfig);

        Config config = new Config();
        config.getInput().setTestSuites(testSuites);
        config.getWebdriver().setConfigs(webDriverConfigs);

        Driver driver = new Driver(
                "/Users/user/sideex-things/runner-testcase/runner-executable-file-oneline/sideex-runner-macos-arm64", config);

        JsonNode report = driver.run();
        String browserNameInJsonReport = report.fieldNames().next();
//        System.out.println("report: " + report);
//        System.out.println("report.get(browserNameInJsonReport): " + report.get(browserNameInJsonReport));

        String resultSuite = report.get(browserNameInJsonReport).get(0).get("suites").get(0).get("status").asText();

        ResultSideex resultSideex = new ResultSideex();
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("resultSuite: " + resultSuite);

        resultSideex.jsonReport = objectMapper.writeValueAsString(report);
        resultSideex.successfulStatus = resultSuite.equals("success") ? true : false;

        return resultSideex;
    }

    @Override
    public SampleResult sample(Entry entry) {
        // Main input start here
        //        res.sampleStart();
        // sampleStart write in new SideexSamplerResult();
        SideexSamplerResult res = new SideexSamplerResult();
        String testCaseFilePath = getTestCaseFilePath();

        // because chrome, firefox's browserName need lowercase
        String browserName = getBrowserSelect().equals("Chrome") || getBrowserSelect().equals("Firefox") ? getBrowserSelect().toLowerCase() : getBrowserSelect();


        System.out.println("browserName: " + browserName);
        ResultSideex resultSideex = new ResultSideex();

        if(!testCaseFilePath.isEmpty() && !testCaseFilePath.isBlank()) {

            try {
                resultSideex = startRunSideex(testCaseFilePath, browserName);

            } catch (Exception e) {
                throw new RuntimeException(e);

            }
            // here set the result of json
            res.setResponseMessage(resultSideex.jsonReport);

        }

//        res.setSampleLabel(this.getPropertyAsString(NAME));
        res.setSampleLabel(getName());

        res.setSuccessful(resultSideex.successfulStatus);
        res.setResponseCode(getResponseCode());

        res.sampleEnd();
        return res;
    }

}
