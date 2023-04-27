package team.sideex.config;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import team.sideex.JMeterPluginUtils;

import java.awt.*;

public class EdgeConfigGui extends AbstractConfigGui {

    private EdgeConfigPanel edgeConfigPanel;

    public EdgeConfigGui() {
        createGui();
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        edgeConfigPanel = new EdgeConfigPanel();
        add(edgeConfigPanel.init(), BorderLayout.CENTER);
        edgeConfigPanel.initFields();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        edgeConfigPanel.configure(element);
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        edgeConfigPanel.modifyTestElement(element);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        edgeConfigPanel.initFields();
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
        return JMeterPluginUtils.prefixLabel("Edge Config");
    }

    @Override
    public TestElement createTestElement() {
        EdgeConfig element = new EdgeConfig();
        modifyTestElement(element);
        return element;
    }
}
