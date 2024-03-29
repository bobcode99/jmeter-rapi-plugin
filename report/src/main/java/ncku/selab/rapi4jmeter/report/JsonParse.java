/*
 * @author  HSU
 */
package ncku.selab.rapi4jmeter.report;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;


public class JsonParse {

    // all test results [{}, {}, {}]
    private ArrayList<String> testResults = new ArrayList<>();

    public JSONObject getJson(int index) throws ParseException {

        Object obj = new JSONParser().parse((testResults.get(index)));

        JSONObject json = (JSONObject) obj;

        // former format :
        // {"chrome 112.0.5615.137": [], "firefox 112.0": []}

        // format now:
        // {"version": {"sideex": [4, 0, 0 ], "format": [1, 0, 1 ] }, "reports": [{"title": "date 16-13-30", "browserName": "chrome 112.0.5615.137", ...}, {"title": "date 16-13-30", "browserName": "firefox 112.0", ...}] }
        // but usually will only have one browser if using this jmeter-rapi-plugin
        // {"version": {"sideex": [4, 0, 0 ], "format": [1, 0, 1 ] }, "reports": [{"title": "date 16-13-30", "browserName": "chrome 112.0.5615.137", ...}] }

        JSONArray jsonArray = (JSONArray) json.get("reports");
        JSONObject resultContent = null;
        if (jsonArray != null) {
            resultContent = (JSONObject) jsonArray.get(0);
        }

        return resultContent;

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
