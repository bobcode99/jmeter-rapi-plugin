package ncku.selab.rapi4jmeter.config;

import org.apache.jmeter.testelement.TestElement;
import ncku.selab.rapi4jmeter.JMeterPluginUtils;

import javax.swing.*;
import java.awt.*;

public class BrowserConfigPanel extends JPanel {

    public JTextField browserArgs;

    public BrowserConfigPanel() {
        super(new GridBagLayout());
    }

    public void configure(TestElement element) {
        if (element instanceof BrowserConfig) {
            BrowserConfig browserConfig = (BrowserConfig) element;
            browserArgs.setText(browserConfig.getBrowserAdditionalArgs());
        }
    }

    public void modifyTestElement(TestElement element) {
        if (element instanceof BrowserConfig) {
            BrowserConfig browserConfig = (BrowserConfig) element;
            browserConfig.setBrowserAdditionalArgs(browserArgs.getText());
        }
    }

    public void initFields() {
        browserArgs.setText("headless, disable-gpu, no-sandbox, disable-dev-shm-usage");
    }

    public JPanel init() {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        JMeterPluginUtils.addToPanel(this, labelConstraints, 0, 4, new JLabel("Browser additional argument: ", JLabel.RIGHT));
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 4, browserArgs = new JTextField());
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 5, new JLabel("If using docker environment, need to add \"disable-dev-shm-usage\" to browser."));
        JPanel container = new JPanel(new BorderLayout());
        container.add(this, BorderLayout.NORTH);
        return container;
    }


}
