/*
 * @author  HSU
 */
package team.sideex.report;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;


public class JsonParse {

    private final ArrayList<String> browserVersions = new ArrayList<String>();
    private ArrayList<String> testResults = new ArrayList<String>();
    private String browserVersionName = "";


    public ArrayList<String> getBrowserVersion() throws IOException, ParseException {


        for (int i = 0; i < testResults.size(); i++) {


//            Object browserObj = new JSONParser().parse(new FileReader(testResults.get(i)));
            Object browserObj = new JSONParser().parse(testResults.get(i));


            int pos = browserObj.toString().indexOf("\"");

            browserVersionName = browserObj.toString();
            browserVersionName = browserVersionName.substring(pos + 1);

            int pos2 = browserVersionName.indexOf("\"");


            browserVersionName = browserVersionName.substring(0, pos2);

            if (browserVersions.contains(browserVersionName))
                continue;

            browserVersions.add(browserVersionName);


            browserObj = null;
        }

        return browserVersions;

    }

    public JSONObject getJson(int index) throws IOException, ParseException {

        Object obj = new JSONParser().parse((testResults.get(index)));

        JSONObject json = (JSONObject) obj;

        for (int j = 0; j < browserVersions.size(); j++) {

            JSONArray jsonArray = (JSONArray) json.get(browserVersions.get(j));

            if (jsonArray != null) {
                json = (JSONObject) jsonArray.get(0);
                break;
            }

        }

        obj = null;

        return json;

    }

    public JSONArray getRecordsArray(int index) throws IOException, ParseException {

        JSONObject json = getJson(index);

        JSONArray casesArray = (JSONArray) json.get("cases");
        JSONObject cases = (JSONObject) casesArray.get(0);
        JSONArray recordsArray = (JSONArray) cases.get("records");

        return recordsArray;

    }

    public void addTestResults(ArrayList<String> testResults) {
        this.testResults = testResults;
    }

}
