/*
 * @link    https://github.com/fourcolor/sideex-api-java
 * @author  fourcolor
 */

package team.sideex.api.config;

import java.util.ArrayList;

public class WebDriverConfig {
    private String type = "selenium";
    private String serverUrl = "";
    private ArrayList<Browser> browsers = new ArrayList<Browser>();
    private ArrayList<Session> sessions = new ArrayList<Session>();

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
