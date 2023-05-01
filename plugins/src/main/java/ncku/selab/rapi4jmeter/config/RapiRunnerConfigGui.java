package ncku.selab.rapi4jmeter.config;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import ncku.selab.rapi4jmeter.JMeterPluginUtils;

import java.awt.*;

public class RapiRunnerConfigGui extends AbstractConfigGui {

    private RapiRunnerConfigPanel rapiRunnerConfigPanel;

    public RapiRunnerConfigGui() {
        createGui();
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        rapiRunnerConfigPanel = new RapiRunnerConfigPanel();
        add(rapiRunnerConfigPanel.init(), BorderLayout.CENTER);
        rapiRunnerConfigPanel.initFields();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        rapiRunnerConfigPanel.configure(element);
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        rapiRunnerConfigPanel.modifyTestElement(element);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        rapiRunnerConfigPanel.initFields();
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
        return JMeterPluginUtils.prefixLabel("Rapi Runner Config");
    }

    @Override
    public TestElement createTestElement() {
        RapiRunnerConfig element = new RapiRunnerConfig();
        modifyTestElement(element);
        return element;
    }
}
