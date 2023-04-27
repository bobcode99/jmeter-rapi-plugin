package team.sideex.config;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import team.sideex.JMeterPluginUtils;

import java.awt.*;

public class ChromeConfigGui extends AbstractConfigGui {

    private ChromeConfigPanel chromeConfigPanel;

    public ChromeConfigGui() {
        createGui();
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        chromeConfigPanel = new ChromeConfigPanel();
        add(chromeConfigPanel.init(), BorderLayout.CENTER);
        chromeConfigPanel.initFields();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        chromeConfigPanel.configure(element);
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        chromeConfigPanel.modifyTestElement(element);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        chromeConfigPanel.initFields();
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
        return JMeterPluginUtils.prefixLabel("Chrome Config");
    }

    @Override
    public TestElement createTestElement() {
        ChromeConfig element = new ChromeConfig();
        modifyTestElement(element);
        return element;
    }
}
