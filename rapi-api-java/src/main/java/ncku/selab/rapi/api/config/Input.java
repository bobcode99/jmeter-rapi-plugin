/*
 * @link    https://github.com/RapiTest/rapi-api
 * @author  fourcolor
 */

package ncku.selab.rapi.api.config;

import java.util.ArrayList;

public class Input {
    private ArrayList<String> testSuites = new ArrayList<>();
    private ArrayList<String> variables = new ArrayList<>();
    private ArrayList<String> dataDriven = new ArrayList<>();

    public ArrayList<String> getDataDriven() {
        return dataDriven;
    }

    public void setDataDriven(ArrayList<String> dataDriven) {
        this.dataDriven = dataDriven;
    }

    public ArrayList<String> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(ArrayList<String> testSuites) {
        this.testSuites = testSuites;
    }

    public ArrayList<String> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<String> variables) {
        this.variables = variables;
    }
}
