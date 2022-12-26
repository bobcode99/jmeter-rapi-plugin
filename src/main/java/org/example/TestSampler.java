package org.example;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.logging.log4j.util.Strings;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestSampler extends AbstractSampler {

    private static final Logger LOG = LoggerFactory.getLogger(TestSampler.class);

    private static final String LABEL = "LABEL";
    private static final String RESPONSE_CODE = "RESPONSE_CODE";
    private static final String SUCCESS = "SUCCESS";
    private static final String RESPONSE_MESSAGE = "RESPONSE_MESSAGE";
    private static final String TC_FILE_PATH = "TC_FILE_PATH";

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

    public static String getJsonString(String filePath) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String stringJsonObj = null;
        try (FileReader reader = new FileReader(filePath)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject myJsonObj = (JSONObject) obj;
            stringJsonObj = myJsonObj.toString();
            System.out.println(stringJsonObj);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringJsonObj;
    }

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult res = new SampleResult();
        res.sampleStart();

        // get file contents
        String testCaseFilePath = getTestCaseFilePath();
        System.out.println("testCaseFilePath: " + testCaseFilePath);

        String resultJsonString = getJsonString(testCaseFilePath);

        res.setSampleLabel(getLabel());
        res.setSuccessful(getSuccessful());
        res.setResponseCode(getResponseCode());

        res.setResponseMessage(getResponseMessage());
//        res.setResponseMessage(resultJsonString);
        // here set the result of json
        res.setResponseData(resultJsonString, null);

        res.sampleEnd();
        return res;
    }

}
