package ncku.selab.rapi4jmeter.config;

public class ChromeConfigPanel extends BrowserConfigPanel {

    public ChromeConfigPanel() {
        super();
    }

    @Override
    public void initFields() {
        // Set the default arguments for Chrome
        browserArgs.setText("--headles, --disable-gpu, --no-sandbox, --disable-dev-shm-usage");
    }
}
