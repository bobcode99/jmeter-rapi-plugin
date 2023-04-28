package team.sideex.sampler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.sideex.api.Driver;
import team.sideex.api.config.Browser;
import team.sideex.api.config.Config;
import team.sideex.api.config.WebDriverConfig;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static team.sideex.api.Driver.getBrowserArgs;
import static team.sideex.api.Driver.getBrowserOptions;

public class SideexSampler extends AbstractSampler {

    private static final Logger LOG = LoggerFactory.getLogger(SideexSampler.class);
    private static final String RESPONSE_CODE = "RESPONSE_CODE";
    private static final String TC_FILE_PATH = "TC_FILE_PATH";
    private static final String BROWSER_SELECT = "BROWSER_SELECT";
    private static final String ENABLE_LOG = "ENABLE_LOG";

    public boolean getEnableLog() {
        return getPropertyAsBoolean(ENABLE_LOG);
    }

    public void setEnableLog(boolean isEnable) {
        setProperty(ENABLE_LOG, isEnable);
    }

    public String getResponseCode() {
        return getPropertyAsString(RESPONSE_CODE, "200");
    }

    public String getTestCaseFilePath() {
        return getPropertyAsString(TC_FILE_PATH, "");
    }

    public void setTestCaseFilePath(String filePath) {
        setProperty(TC_FILE_PATH, filePath);
    }

    public String getBrowserSelect() {
        return getPropertyAsString(BROWSER_SELECT, "Firefox");
    }

    public void setBrowserSelect(String browserName) {
        setProperty(BROWSER_SELECT, browserName);
    }

    private String getBrowserArgsContent(String browserName) {
        switch (browserName) {
            case "chrome":
                return "BROWSER_ADDITIONAL_ARGS_FOR_SIDEEX_USE_CHROME_CONFIG";
            case "firefox":
                return "BROWSER_ADDITIONAL_ARGS_FOR_SIDEEX_USE_FIREFOX_CONFIG";
            case "MicrosoftEdge":
                return "BROWSER_ADDITIONAL_ARGS_FOR_SIDEEX_USE_EDGE_CONFIG";
        }
        return browserName;
    }

    private ResultSideex startRunSideex(String testCase, String browserName) throws Exception {
        // For get config settings
        JMeterContext context = getThreadContext();
        JMeterVariables jMeterVariables = context.getVariables();

        ArrayList<String> testSuites = new ArrayList<>();
        testSuites.add(testCase);

        Browser browser = new Browser();
        Map<String, Object> caps = new HashMap<>();

        // browserName: {"chrome", "firefox", "MicrosoftEdge"}
        caps.put("browserName", browserName);
        HashMap<String, ArrayList<String>> browserArgs = new HashMap<>();

        // setBrowserArgs: "args": ["-headless","-disable-gpu", "-window-size=1080,720"]

        String variableNameJmeterGet = getBrowserArgsContent(browserName);

        LOG.info("variableNameJmeterGet: " + variableNameJmeterGet);
        LOG.info("jMeterVariables.get chrome: " + jMeterVariables.get("BROWSER_ADDITIONAL_ARGS_FOR_SIDEEX_USE_CHROME_CONFIG"));
        LOG.info("jMeterVariables.get firefox: " + jMeterVariables.get("BROWSER_ADDITIONAL_ARGS_FOR_SIDEEX_USE_FIREFOX_CONFIG"));
        LOG.info("jMeterVariables.get edge: " + jMeterVariables.get("BROWSER_ADDITIONAL_ARGS_FOR_SIDEEX_USE_EDGE_CONFIG"));

        LOG.info("jMeterVariables.get(variableNameJmeterGet): " + jMeterVariables.get(variableNameJmeterGet));


        browserArgs.put("args", getBrowserArgs(browserName, jMeterVariables.get( variableNameJmeterGet )));

        // set browserOptions: "moz:firefoxOptions": {"args": ["-headless","-disable-gpu", "-window-size=1080,720"]}
        caps.put(getBrowserOptions(browserName), browserArgs);

        browser.setCapability(caps);

//        System.out.println("browser.getCapability: " + browser.getCapability());
        LOG.info("browser.getCapability: " + browser.getCapability());

        ArrayList<Browser> browsers = new ArrayList<>();
        browsers.add(browser);

        WebDriverConfig webDriverConfig = new WebDriverConfig();
        webDriverConfig.setBrowsers(browsers);

//        System.out.println("jMeterVariables RUNNER_EXE_PATH: " + jMeterVariables.get("RUNNER_EXE_PATH_FOR_SIDEEX_USE"));
//        System.out.println("jMeterVariables SELENIUM_PORT: " + jMeterVariables.get("SELENIUM_PORT_FOR_SIDEEX_USE"));

        webDriverConfig.setServerUrl(jMeterVariables.get("SELENIUM_PORT_FOR_SIDEEX_USE"));
        ArrayList<WebDriverConfig> webDriverConfigs = new ArrayList<>();
        webDriverConfigs.add(webDriverConfig);

        Config config = new Config();
        config.getInput().setTestSuites(testSuites);
        config.getWebdriver().setConfigs(webDriverConfigs);
        config.getPlay().setAutoWaitTimeout(30);
        config.getPlay().setMode(2);

        // Note that if executable path doesn't exist, it will not print any warning message.
        // TODO: add file check exist
        Driver driver = new Driver(
                jMeterVariables.get("RUNNER_EXE_PATH_FOR_SIDEEX_USE"), config);

        JsonNode report = driver.run();

        // runner report format: {"version":{"sideex":[4,0,0],"format":[1,0,1]},"reports":[{}]}
        String reportFieldNames = "reports";

        String resultSuite = report.get(reportFieldNames).get(0).get("suites").get(0).get("status").asText();
        ArrayNode reportContent = (ArrayNode) report.get(reportFieldNames);

        // if enable log is false will remove the report's logs.
        if(!getEnableLog()) {
            // remove logs
            for (JsonNode node : reportContent) {
                ObjectNode object = (ObjectNode) node;
                object.remove("logs");
            }
        }

        try {
            // requestsNode =  {"requests":["GET https://search.yahoo.com/","GET https://s.yimg.com/oa/consent.js"],"ammountOfRequest":22}
            JsonNode requestsNode = report.at("/" + reportFieldNames  + "/0/requestResult");

            String requstsFilePath = "/tmp/requests.json";
            // Write the contents of requestsNode into a file named "requests.txt"
            File file = new File(requstsFilePath);
            FileWriter writer = new FileWriter(file);
            writer.write(requestsNode.toString());
            writer.close();

            LOG.info("Successful write requests file to " + requstsFilePath);

            // remove requests
            for (JsonNode node : reportContent) {
                ObjectNode requestResultObject = (ObjectNode) node.get("requestResult");
                requestResultObject.remove("requests");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            LOG.error("Generate requests step error: " + e);
        }

        ResultSideex resultSideex = new ResultSideex();
        ObjectMapper objectMapper = new ObjectMapper();

//        System.out.println("resultSuite: " + resultSuite);

        resultSideex.jsonReport = objectMapper.writeValueAsString(report);
        resultSideex.successfulStatus = resultSuite.equals("success");

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

//        System.out.println("browserName: " + browserName);
        ResultSideex resultSideex = new ResultSideex();
        if (!testCaseFilePath.isEmpty() && !testCaseFilePath.isBlank()) {

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

    static class ResultSideex {
        private String jsonReport;
        private boolean successfulStatus;
    }

}
