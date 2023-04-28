package team.sideex.config;

public class ChromeConfig extends BrowserConfig {
    @Override
    protected String getBrowserAdditionalArgsVariableName() {
        return "BROWSER_ADDITIONAL_ARGS_FOR_SIDEEX_USE_CHROME_CONFIG";
    }
}

