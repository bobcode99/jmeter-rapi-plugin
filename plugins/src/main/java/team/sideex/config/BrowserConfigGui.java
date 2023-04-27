package team.sideex.config;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import team.sideex.JMeterPluginUtils;

import java.awt.*;

public abstract class BrowserConfigGui extends AbstractConfigGui {

    private BrowserConfigPanel browserConfigPanel;

    public BrowserConfigGui(BrowserConfigPanel browserConfigPanel) {
        this.browserConfigPanel = browserConfigPanel;
        createGui();
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
//        this.browserConfigPanel = new BrowserConfigPanel();
        add(this.browserConfigPanel.init(), BorderLayout.CENTER);
        this.browserConfigPanel.initFields();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        this.browserConfigPanel.configure(element);
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        this.browserConfigPanel.modifyTestElement(element);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        this.browserConfigPanel.initFields();
    }


//    @Override
//    public void itemStateChanged(ItemEvent e) {
//
//    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        //set display name
//        System.out.println("getStaticLabel");
        return JMeterPluginUtils.prefixLabel("ChromeConfig");
    }

    @Override
    public TestElement createTestElement() {
        ChromeConfig element = new ChromeConfig();
        modifyTestElement(element);
        return element;
    }
}
