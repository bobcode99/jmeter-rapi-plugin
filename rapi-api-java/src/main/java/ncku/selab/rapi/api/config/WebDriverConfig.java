/*
 * @link    https://github.com/RapiTest/rapi-api
 * @author  fourcolor
 */

package ncku.selab.rapi.api.config;

import java.util.ArrayList;

public class WebDriverConfig {
    private String type = "selenium";
    private String serverUrl = "";
    private ArrayList<Browser> browsers = new ArrayList<>();
    private ArrayList<Session> sessions = new ArrayList<>();

    public ArrayList<Browser> getBrowsers() {
        return browsers;
    }

    public void setBrowsers(ArrayList<Browser> browsers) {
        this.browsers = browsers;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type.equals("selenium") || type.equals("appium"))
            this.type = type;
        else {
            throw new Error("Unknown support webDriver config type " + type);
        }
    }
}
