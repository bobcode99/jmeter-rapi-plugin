/*
 * @author  HSU
 */
package team.sideex.report;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;


public class JsonParse {

    private final ArrayList<String> browserVersions = new ArrayList<>();
    private ArrayList<String> testResults = new ArrayList<>();


    public ArrayList<String> getBrowserVersion() throws ParseException {


        for (String testResult : testResults) {


//            Object browserObj = new JSONParser().parse(new FileReader(testResults.get(i)));
            Object browserObj = new JSONParser().parse(testResult);


            int pos = browserObj.toString().indexOf("\"");

            String browserVersionName = browserObj.toString();
            browserVersionName = browserVersionName.substring(pos + 1);

            int pos2 = browserVersionName.indexOf("\"");


            browserVersionName = browserVersionName.substring(0, pos2);

            if (browserVersions.contains(browserVersionName))
                continue;

            browserVersions.add(browserVersionName);


        }

        return browserVersions;

    }

    public JSONObject getJson(int index) throws ParseException {

        Object obj = new JSONParser().parse((testResults.get(index)));

        JSONObject json = (JSONObject) obj;

        for (String browserVersion : browserVersions) {

            JSONArray jsonArray = (JSONArray) json.get(browserVersion);

            if (jsonArray != null) {
                json = (JSONObject) jsonArray.get(0);
                break;
            }

        }

        return json;

    }

    public JSONArray getRecordsArray(int index) throws ParseException {

        JSONObject json = getJson(index);

        JSONArray casesArray = (JSONArray) json.get("cases");
        JSONObject cases = (JSONObject) casesArray.get(0);

        return (JSONArray) cases.get("records");

    }

    public void addTestResults(ArrayList<String> testResults) {
        this.testResults = testResults;
    }

}
