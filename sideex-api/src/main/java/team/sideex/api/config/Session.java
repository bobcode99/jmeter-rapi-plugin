/*
 * @link    https://github.com/fourcolor/sideex-api-java
 * @author  fourcolor
 */

package team.sideex.api.config;

public class Session {
    private String sessionId = "";
    private boolean keepSessionAlive = false;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean getKeepSessionAlive() {
        return keepSessionAlive;
    }

    public void setKeepSessionAlive(boolean keepSessionAlive) {
        this.keepSessionAlive = keepSessionAlive;
    }
}
