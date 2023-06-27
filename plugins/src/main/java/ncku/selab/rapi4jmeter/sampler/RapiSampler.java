package ncku.selab.rapi4jmeter.sampler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ncku.selab.rapi.api.Rapi;
import ncku.selab.rapi.api.RapiReport;
import ncku.selab.rapi.api.config.*;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RapiSampler extends AbstractSampler {

    private static final Logger LOG = LoggerFactory.getLogger(RapiSampler.class);
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
                return "BROWSER_ADDITIONAL_ARGS_FOR_RAPI_USE_CHROME_CONFIG";
            case "firefox":
                return "BROWSER_ADDITIONAL_ARGS_FOR_RAPI_USE_FIREFOX_CONFIG";
            case "MicrosoftEdge":
                return "BROWSER_ADDITIONAL_ARGS_FOR_RAPI_USE_EDGE_CONFIG";
        }
        return browserName;
    }

    private String getBrowserOptions(String browserName) {
        HashMap<String, String> browserOptions = new HashMap<>();
        browserOptions.put("chrome", "goog:chromeOptions");
        browserOptions.put("firefox", "moz:firefoxOptions");
        browserOptions.put("MicrosoftEdge", "ms:edgeOptions");
        return browserOptions.get(browserName);
    }

    private ResultRapi startRunRapi(String testCase, String browserName) throws Exception {
        // For get config settings
        JMeterContext context = getThreadContext();
        JMeterVariables jMeterVariables = context.getVariables();

        ArrayList<String> testSuites = new ArrayList<>();
        testSuites.add(testCase);

        Input inputRapiTest = Input.builder().withTestSuites(testSuites).build();

        Map<String, Object> caps = new HashMap<>();

        // browserName: {"chrome", "firefox", "MicrosoftEdge"}
        caps.put("browserName", browserName);

        // setBrowserArgs: "args": ["-headless","-disable-gpu", "-window-size=1080,720"]

        String variableNameJmeterGet = getBrowserArgsContent(browserName);

        LOG.info("variableNameJmeterGet: " + variableNameJmeterGet);
        LOG.info("jMeterVariables.get chrome: " + jMeterVariables.get("BROWSER_ADDITIONAL_ARGS_FOR_RAPI_USE_CHROME_CONFIG"));
        LOG.info("jMeterVariables.get firefox: " + jMeterVariables.get("BROWSER_ADDITIONAL_ARGS_FOR_RAPI_USE_FIREFOX_CONFIG"));
        LOG.info("jMeterVariables.get edge: " + jMeterVariables.get("BROWSER_ADDITIONAL_ARGS_FOR_RAPI_USE_EDGE_CONFIG"));
        String browserArgsString = jMeterVariables.get(variableNameJmeterGet);

        LOG.info("jMeterVariables.get(variableNameJmeterGet): " + browserArgsString);
        ArrayList<String> browserArgsList = new ArrayList<String>();

        if(browserArgsString == null ) {
            LOG.info("You may want to add " + browserName + " config.");
        } else {
            if (!browserArgsString.isEmpty()) {
                browserArgsList = new ArrayList<>(Arrays.asList(browserArgsString.split(",")));
            }
        }

        HashMap<String, ArrayList<String>> browserArgs = new HashMap<>();
        browserArgs.put("args", browserArgsList);
        LOG.info("browserArgs: " + browserArgs);

        // set browserOptions: "moz:firefoxOptions": {"args": ["-headless","-disable-gpu", "-window-size=1080,720"]}
        caps.put(getBrowserOptions(browserName), browserArgs);
        ArrayList<WebDriverCommonConfig> service = new ArrayList<>();
        service.add(
                WebDriverBrowserConfig.builder()
                        .withBrowsers(Browser.builder().withCapability(caps).build())
                        .withServerUrl(jMeterVariables.get("SELENIUM_URL_FOR_RAPI_USE")).build());


        Config config = Config.builder().withInput(inputRapiTest)
                .withWebdriver(WebDriver.builder().withConfigs(service).build()).build();

        String rapiConfigStr = config.toString();
        System.out.println("rapiConfigStr: " + rapiConfigStr);
        LOG.info("Rapi config: ");
        LOG.info(rapiConfigStr);
        // TODO: add file check exist
        // Note that if executable path doesn't exist, it will not print any warning message.
        Rapi rapi = new Rapi(jMeterVariables.get("RUNNER_EXE_PATH_FOR_RAPI_USE"), config);
        RapiReport report = rapi.run();
        JsonNode rapiResultJsonNode = report.getJson();

        // runner report format: {"version":{"sideex":[4,0,0],"format":[1,0,1]},"reports":[{}]}
        String reportFieldNames = "reports";

        String resultSuite = rapiResultJsonNode.get(reportFieldNames).get(0).get("suites").get(0).get("status").asText();
        ArrayNode reportContent = (ArrayNode) rapiResultJsonNode.get(reportFieldNames);

        // if enable log is false will remove the report's logs.
        if(!getEnableLog()) {
            // remove logs
            for (JsonNode node : reportContent) {
                ObjectNode object = (ObjectNode) node;
                object.remove("logs");
            }
        }

        ResultRapi resultRapi = new ResultRapi();
        ObjectMapper objectMapper = new ObjectMapper();

//        System.out.println("resultSuite: " + resultSuite);

        resultRapi.jsonReport = objectMapper.writeValueAsString(rapiResultJsonNode);
        resultRapi.successfulStatus = resultSuite.equals("success");

        LOG.info(objectMapper.writeValueAsString(rapiResultJsonNode));

        LOG.info("End test");
        return resultRapi;
    }

    @Override
    public SampleResult sample(Entry entry) {
        // Main input start here
        //        res.sampleStart();
        // sampleStart write in new RapiSamplerResult();
        RapiSamplerResult res = new RapiSamplerResult();
        String testCaseFilePath = getTestCaseFilePath();

        // because chrome, firefox's browserName need lowercase
        String browserName = getBrowserSelect().equals("Chrome") || getBrowserSelect().equals("Firefox") ? getBrowserSelect().toLowerCase() : getBrowserSelect();

//        System.out.println("browserName: " + browserName);
        ResultRapi resultRapi = new ResultRapi();
        if (!testCaseFilePath.isEmpty() && !testCaseFilePath.isBlank()) {

            try {
                resultRapi = startRunRapi(testCaseFilePath, browserName);

            } catch (Exception e) {
                throw new RuntimeException(e);

            }
            // here set the result of json
            res.setResponseMessage(resultRapi.jsonReport);

        }

//        res.setSampleLabel(this.getPropertyAsString(NAME));
        res.setSampleLabel(getName());

        res.setSuccessful(resultRapi.successfulStatus);
        res.setResponseCode(getResponseCode());

        res.sampleEnd();
        return res;
    }

    static class ResultRapi {
        private String jsonReport;
        private boolean successfulStatus;
    }

}
