package team.sideex.sampler;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.logging.log4j.util.Strings;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

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

    public static String getJsonString(String filePath) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String stringJsonObj = null;
        try (FileReader reader = new FileReader(filePath)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject myJsonObj = (JSONObject) obj;
            stringJsonObj = myJsonObj.toString();
            System.out.println("getJsonString: " + stringJsonObj);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return stringJsonObj;
    }

    @Override
    public SampleResult sample(Entry entry) {
        SideexSamplerResult res = new SideexSamplerResult();
//        res.sampleStart();

        // get file contents
        String testCaseFilePath = getTestCaseFilePath();
//        System.out.println("testCaseFilePath: " + testCaseFilePath);
//        System.out.println(testCaseFilePath);
        String browserName = getBrowserSelect();
        System.out.println("browserName: " + browserName);

        if(!testCaseFilePath.isEmpty() && !testCaseFilePath.isBlank()) {
            // here set the result of json
            // res.setResponseMessage(resultJsonString);
            System.out.println("write response data !");
            String resultJsonString = getJsonString(testCaseFilePath);
            res.setResponseData(resultJsonString, null);
        }

        res.setSampleLabel(getLabel());
        res.setSuccessful(getSuccessful());
        res.setResponseCode(getResponseCode());
        res.setResponseMessage(getResponseMessage());
        res.sampleEnd();
        return res;
    }

}
