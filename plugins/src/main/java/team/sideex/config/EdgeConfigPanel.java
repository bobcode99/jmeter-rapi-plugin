package team.sideex.config;

import org.apache.jmeter.testelement.TestElement;
import team.sideex.JMeterPluginUtils;

import javax.swing.*;
import java.awt.*;

public class EdgeConfigPanel extends JPanel {

    private JTextField browserArgs;

    public EdgeConfigPanel() {
        super(new GridBagLayout());
    }

    public void configure(TestElement element) {
        if (element instanceof EdgeConfig) {
            EdgeConfig edgeConfig = (EdgeConfig) element;
            browserArgs.setText(edgeConfig.getBrowserAdditionalArgs());
        }
    }

    public void modifyTestElement(TestElement element) {
        if (element instanceof EdgeConfig) {
            EdgeConfig edgeConfig = (EdgeConfig) element;
            edgeConfig.setBrowserAdditionalArgs(browserArgs.getText());
        }
    }

    public void initFields() {

//        browserArgs.setText("headless, disable-gpu, no-sandbox, disable-dev-shm-usage");
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
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 6, new JLabel("If using Firefox, need to add \"-\" on each args."));

        JPanel container = new JPanel(new BorderLayout());
        container.add(this, BorderLayout.NORTH);
        return container;
    }


}
