package team.sideex.config;

import org.apache.jmeter.testelement.TestElement;
import team.sideex.JMeterPluginUtils;

import javax.swing.*;
import java.awt.*;

public class SideexRunnerConfigPanel extends JPanel {

    private JTextField runnerExePath;
    private JTextField seleniumPath;
    private JTextField browserArgs;

    public SideexRunnerConfigPanel() {
        super(new GridBagLayout());
    }

    public void configure(TestElement element) {
        if (element instanceof SideexRunnerConfig sideexRunnerConfig) {
            runnerExePath.setText(sideexRunnerConfig.getRunnerExePath());
            seleniumPath.setText(sideexRunnerConfig.getSeleniumPort());
            browserArgs.setText(sideexRunnerConfig.getBrowserAdditionalArgs());
        }
    }

    public void modifyTestElement(TestElement element) {
        if (element instanceof SideexRunnerConfig sideexRunnerConfig) {
            sideexRunnerConfig.setRunnerExePath(runnerExePath.getText());
            sideexRunnerConfig.setSeleniumPort(seleniumPath.getText());
            sideexRunnerConfig.setBrowserAdditionalArgs(browserArgs.getText());
        }
    }

    public void initFields() {
        runnerExePath.setText("/path/to/sideex-runner-exe");
        seleniumPath.setText("http://127.0.0.1:4444");
        browserArgs.setText("headless, disable-gpu, window-size=1080,720, no-sandbox, disable-dev-shm-usage");
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
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 1, new JLabel("Make sure the path is correct. Example: /path/to/sideex-runner-linux"));

        JMeterPluginUtils.addToPanel(this, labelConstraints, 0, 2, new JLabel("Selenium port: ", JLabel.RIGHT));
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 2, seleniumPath = new JTextField());
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 3, new JLabel("Follow the example. Example: http://127.0.0.1:4445"));

        JMeterPluginUtils.addToPanel(this, labelConstraints, 0, 4, new JLabel("Browser additional argument: ", JLabel.RIGHT));
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 4, browserArgs = new JTextField());
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 5, new JLabel("If using docker environment, need to add \"disable-dev-shm-usage\" to browser."));
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 6, new JLabel("If using Firefox, need to add \"-\" on each args." +
                "Note that some args browser will not supported"));

        JPanel container = new JPanel(new BorderLayout());
        container.add(this, BorderLayout.NORTH);
        return container;
    }


}