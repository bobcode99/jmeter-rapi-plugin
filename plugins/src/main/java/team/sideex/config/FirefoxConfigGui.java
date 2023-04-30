package team.sideex.config;

import org.apache.jmeter.testelement.TestElement;

public class FirefoxConfigGui extends BrowserConfigGui {

    @Override
    protected BrowserConfigPanel createBrowserConfigPanel() {
        return new FirefoxConfigPanel();
    }

    @Override
    protected String getBrowserName() {
        return "Firefox";
    }

    @Override
    protected BrowserConfig createBrowserConfig() {
        return new FirefoxConfig();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        // additional configuration specific to ChromeConfigGui
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        // additional modifications specific to ChromeConfigGui
    }

}
