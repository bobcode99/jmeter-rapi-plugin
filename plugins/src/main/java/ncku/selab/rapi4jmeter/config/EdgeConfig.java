package ncku.selab.rapi4jmeter.config;

public class EdgeConfig extends BrowserConfig {
    @Override
    protected String getBrowserAdditionalArgsVariableName() {
        return "BROWSER_ADDITIONAL_ARGS_FOR_RAPI_USE_EDGE_CONFIG";
    }
}
