/*
 * @link    https://github.com/RapiTest/rapi-api
 * @author  fourcolor
 */

package ncku.selab.rapi.api.config;

import java.util.Collections;
import java.util.Map;

public class Browser {
    private final boolean active = true;
    private boolean keepSessionAlive = false;
    private Map<String, Object> capability = Collections.emptyMap();

    public Map<String, Object> getCapability() {
        return capability;
    }

    public void setCapability(Map<String, Object> capability) {
        this.capability = capability;
    }

    public boolean getKeepSessionAlive() {
        return keepSessionAlive;
    }

    public void setKeepSessionAlive(boolean keepSessionAlive) {
        this.keepSessionAlive = keepSessionAlive;
    }
}
