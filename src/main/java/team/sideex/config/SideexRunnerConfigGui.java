package team.sideex.config;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import team.sideex.JMeterPluginUtils;
import team.sideex.sampler.SideexSampler;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SideexRunnerConfigGui extends AbstractConfigGui {

    private SideexRunnerConfigPanel sideexRunnerConfigPanel;

    public SideexRunnerConfigGui() {
        createGui();
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        sideexRunnerConfigPanel = new SideexRunnerConfigPanel();
        add(sideexRunnerConfigPanel.init(), BorderLayout.CENTER);
        sideexRunnerConfigPanel.initFields();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        sideexRunnerConfigPanel.configure(element);
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        sideexRunnerConfigPanel.modifyTestElement(element);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        sideexRunnerConfigPanel.initFields();
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
        return JMeterPluginUtils.prefixLabel("SideexRunnerConfig");
    }

    @Override
    public TestElement createTestElement() {
        SideexRunnerConfig element = new SideexRunnerConfig();
        modifyTestElement(element);
        return element;
    }
}
