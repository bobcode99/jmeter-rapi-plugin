package ncku.selab.rapi4jmeter.config;

public class FirefoxConfigPanel extends BrowserConfigPanel {

    public FirefoxConfigPanel() {
        super();
    }

    @Override
    public void initFields() {
        // Set the default arguments for Firefox
        browserArgs.setText("-headless");
    }
}
