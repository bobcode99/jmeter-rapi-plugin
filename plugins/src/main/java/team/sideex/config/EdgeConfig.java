package team.sideex.config;

public class EdgeConfig extends BrowserConfig {
    @Override
    protected String getBrowserAdditionalArgsVariableName() {
        return "BROWSER_ADDITIONAL_ARGS_FOR_SIDEEX_USE_EDGE_CONFIG";
    }
}
