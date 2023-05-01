package ncku.selab.rapi4jmeter.config;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import ncku.selab.rapi4jmeter.JMeterPluginUtils;

import java.awt.*;

public abstract class BrowserConfigGui extends AbstractConfigGui {

    protected BrowserConfigPanel browserConfigPanel;

    public BrowserConfigGui() {
        createGui();
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        browserConfigPanel = createBrowserConfigPanel();
        add(browserConfigPanel.init(), BorderLayout.CENTER);
        browserConfigPanel.initFields();
    }

    protected abstract BrowserConfigPanel createBrowserConfigPanel();

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        browserConfigPanel.configure(element);
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        browserConfigPanel.modifyTestElement(element);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        browserConfigPanel.initFields();
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        //set display name
        return JMeterPluginUtils.prefixLabel(getBrowserName() + " Config");
    }

    protected abstract String getBrowserName();

    @Override
    public TestElement createTestElement() {
        BrowserConfig element = createBrowserConfig();
        modifyTestElement(element);
        return element;
    }

    protected abstract BrowserConfig createBrowserConfig();

}
