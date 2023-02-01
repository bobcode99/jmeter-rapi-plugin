package team.sideex.config;

import org.apache.jmeter.testelement.TestElement;
import team.sideex.JMeterPluginUtils;

import javax.swing.*;
import java.awt.*;

public class SideexRunnerConfigPanel extends JPanel {

    private JTextField runnerExePath;
    private JTextField seleniumPath;


    public SideexRunnerConfigPanel() {
        super(new GridBagLayout());
    }

    public void configure(TestElement element) {
        if (element instanceof SideexRunnerConfig) {
            SideexRunnerConfig sideexRunnerConfig = (SideexRunnerConfig) element;
            runnerExePath.setText(sideexRunnerConfig.getRunnerExePath());
            seleniumPath.setText(sideexRunnerConfig.getSeleniumPort());
        }
    }

    public void modifyTestElement(TestElement element) {
        if (element instanceof SideexRunnerConfig) {
            SideexRunnerConfig sideexRunnerConfig = (SideexRunnerConfig) element;
            sideexRunnerConfig.setRunnerExePath(runnerExePath.getText());
            sideexRunnerConfig.setSeleniumPort(seleniumPath.getText());
        }
    }

    public void initFields() {
        runnerExePath.setText("/path/to/sideex-runner-exe");
        seleniumPath.setText("http://127.0.0.1:4444");
    }

    public JPanel init() {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        JMeterPluginUtils.addToPanel(this, labelConstraints, 0, 0, new JLabel("Sideex runner executable path: ", JLabel.RIGHT));
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 0, runnerExePath = new JTextField());


        JMeterPluginUtils.addToPanel(this, labelConstraints, 0, 1, new JLabel("Selenium port: ", JLabel.RIGHT));
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 1, seleniumPath = new JTextField());

        JPanel container = new JPanel(new BorderLayout());
        container.add(this, BorderLayout.NORTH);
        return container;
    }


}
