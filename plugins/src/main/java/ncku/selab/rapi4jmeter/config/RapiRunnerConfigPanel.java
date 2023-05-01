package ncku.selab.rapi4jmeter.config;

import org.apache.jmeter.testelement.TestElement;
import ncku.selab.rapi4jmeter.JMeterPluginUtils;

import javax.swing.*;
import java.awt.*;

public class RapiRunnerConfigPanel extends JPanel {

    private JTextField runnerExePath;
    private JTextField seleniumURL;
//    private JTextField browserArgs;

    public RapiRunnerConfigPanel() {
        super(new GridBagLayout());
    }

    public void configure(TestElement element) {
        if (element instanceof RapiRunnerConfig) {
            RapiRunnerConfig rapiRunnerConfig = (RapiRunnerConfig) element;
            runnerExePath.setText(rapiRunnerConfig.getRunnerExePath());
            seleniumURL.setText(rapiRunnerConfig.getSeleniumURL());
//            browserArgs.setText(rapiRunnerConfig.getBrowserAdditionalArgs());
        }
    }

    public void modifyTestElement(TestElement element) {
        if (element instanceof RapiRunnerConfig) {
            RapiRunnerConfig rapiRunnerConfig = (RapiRunnerConfig) element;
            rapiRunnerConfig.setRunnerExePath(runnerExePath.getText());
            rapiRunnerConfig.setSeleniumPort(seleniumURL.getText());
//            rapiRunnerConfig.setBrowserAdditionalArgs(browserArgs.getText());
        }
    }

    public void initFields() {
        runnerExePath.setText("/path/to/rapi-runner-exe");
        seleniumURL.setText("http://127.0.0.1:4444");
//        browserArgs.setText("headless, disable-gpu, no-sandbox, disable-dev-shm-usage");
    }

    public JPanel init() {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        JMeterPluginUtils.addToPanel(this, labelConstraints, 0, 0, new JLabel("Rapi runner path: ", JLabel.RIGHT));
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 0, runnerExePath = new JTextField());
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 1, new JLabel("Make sure the path is correct. Example: /path/to/rapi-runner-linux"));

        JMeterPluginUtils.addToPanel(this, labelConstraints, 0, 2, new JLabel("Selenium URL: ", JLabel.RIGHT));
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 2, seleniumURL = new JTextField());
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 3, new JLabel("Follow the example. Example: http://127.0.0.1:4444"));


//        JMeterPluginUtils.addToPanel(this, labelConstraints, 0, 4, new JLabel("Browser additional argument: ", JLabel.RIGHT));
//        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 4, browserArgs = new JTextField());
//        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 5, new JLabel("If using docker environment, need to add \"disable-dev-shm-usage\" to browser."));
//        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 6, new JLabel("If using Firefox, need to add \"-\" on each args."));
//        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 7, new JLabel("Note that some args browser will not supported"));


        JPanel container = new JPanel(new BorderLayout());
        container.add(this, BorderLayout.NORTH);
        return container;
    }


}
