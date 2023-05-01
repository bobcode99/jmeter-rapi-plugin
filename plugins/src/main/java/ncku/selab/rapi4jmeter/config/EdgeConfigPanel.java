package ncku.selab.rapi4jmeter.config;

public class EdgeConfigPanel extends BrowserConfigPanel {

    public EdgeConfigPanel() {
        super();
    }

    @Override
    public void initFields() {
        // Set the default arguments for Edge
        browserArgs.setText("--headless, --disable-gpu, --no-sandbox, --disable-dev-shm-usage");
    }
}
