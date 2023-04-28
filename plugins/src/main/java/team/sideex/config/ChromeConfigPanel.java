package team.sideex.config;

public class ChromeConfigPanel extends BrowserConfigPanel {

    public ChromeConfigPanel() {
        super();
    }

    @Override
    public void initFields() {
        // Set the default arguments for Chrome
        browserArgs.setText("headless, disable-gpu, no-sandbox");
    }
}
