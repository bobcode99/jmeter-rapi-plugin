package team.sideex.config;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import team.sideex.JMeterPluginUtils;
import team.sideex.sampler.SideexSampler;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SideexRunnerConfigGui extends AbstractConfigGui {

    private JTextField runnerExePath;
    private JTextField seleniumPath;

    public SideexRunnerConfigGui() {
        createGui();
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
    }

    @Override
    public void configure(TestElement element) {
        if(element instanceof SideexRunnerConfig) {
            SideexRunnerConfig sideexRunnerConfig = (SideexRunnerConfig) element;
            runnerExePath.setText(sideexRunnerConfig.getRunnerExePath());
            seleniumPath.setText(sideexRunnerConfig.getSeleniumPort());
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        if(element instanceof SideexRunnerConfig) {
            SideexRunnerConfig sideexRunnerConfig = (SideexRunnerConfig) element;
            sideexRunnerConfig.setRunnerExePath(runnerExePath.getText());
            sideexRunnerConfig.setSeleniumPort(seleniumPath.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    public void initFields() {
        runnerExePath.setText("/path/to/sideex-runner-exe");
        seleniumPath.setText("http://127.0.0.1:4445");
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
