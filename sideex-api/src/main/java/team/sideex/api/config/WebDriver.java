/*
 * @link    https://github.com/fourcolor/sideex-api-java
 * @author  fourcolor
 */

package team.sideex.api.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class WebDriver {
    private ArrayList<WebDriverConfig> configs = new ArrayList<>();

    private Boolean bmp = true;
    private Map<String, String> i18n = Collections.emptyMap();

    public ArrayList<WebDriverConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(ArrayList<WebDriverConfig> configs) {
        this.configs = configs;
    }

    public Map<String, String> getI18n() {
        return i18n;
    }

    public void setI18n(Map<String, String> i18n) {
        this.i18n = i18n;
    }

    public Boolean getBmp() {
        return bmp;
    }
    public void setBmp(boolean bmp) {
        this.bmp = bmp;
    }
}
