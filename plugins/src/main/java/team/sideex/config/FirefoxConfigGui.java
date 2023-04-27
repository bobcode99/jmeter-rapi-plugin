package team.sideex.config;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import team.sideex.JMeterPluginUtils;

import java.awt.*;

public class FirefoxConfigGui extends AbstractConfigGui {

    private FirefoxConfigPanel firefoxCOnfigPanel;

    public FirefoxConfigGui() {
        createGui();
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        firefoxCOnfigPanel = new FirefoxConfigPanel();
        add(firefoxCOnfigPanel.init(), BorderLayout.CENTER);
        firefoxCOnfigPanel.initFields();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        firefoxCOnfigPanel.configure(element);
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        firefoxCOnfigPanel.modifyTestElement(element);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        firefoxCOnfigPanel.initFields();
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
        return JMeterPluginUtils.prefixLabel("Firefox Config");
    }

    @Override
    public TestElement createTestElement() {
        FirefoxConfig element = new FirefoxConfig();
        modifyTestElement(element);
        return element;
    }
}
