package ncku.selab.rapi4jmeter.sampler;

import ncku.selab.rapi4jmeter.JMeterPluginUtils;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class RapiSamplerPanel extends JPanel {
    private JTextField tcFilePath;
    private JComboBox<String> browserSelect;
    private JCheckBox isEnableLog;

    public RapiSamplerPanel() {
        super(new GridBagLayout());
    }

    public void configure(TestElement element) {
        // to read user input

//        System.out.println("PANEL configure");

        if (element instanceof RapiSampler) {
            RapiSampler rapiSampler = (RapiSampler) element;
            tcFilePath.setText(rapiSampler.getTestCaseFilePath());
            browserSelect.setSelectedItem(rapiSampler.getBrowserSelect());
            isEnableLog.setSelected(rapiSampler.getEnableLog());
        }

    }

    public void modifyTestElement(TestElement element) {
        // save user input to Jmeter

//        System.out.println("PANEL modifyTestElement");
        if (element instanceof RapiSampler) {
            RapiSampler rapiSampler = (RapiSampler) element;
            rapiSampler.setTestCaseFilePath(tcFilePath.getText());
            rapiSampler.setBrowserSelect(Objects.requireNonNull(browserSelect.getSelectedItem()).toString());
            rapiSampler.setEnableLog(isEnableLog.isSelected());
        }

    }

    public void initFields() {
        tcFilePath.setText("path/to/testCase.json");
        browserSelect.setSelectedItem("Firefox");
        isEnableLog.setSelected(false);
    }


    public JPanel init() {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        JMeterPluginUtils.addToPanel(this, labelConstraints, 0, 0, new JLabel("Test case file path: ", JLabel.RIGHT));
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 0, tcFilePath = new JTextField());

        JMeterPluginUtils.addToPanel(this, labelConstraints, 0, 1, new JLabel("Select browser to run test: ", JLabel.RIGHT));
        String[] browserList = {"Chrome", "Firefox", "MicrosoftEdge"};

        browserSelect = new JComboBox<>(browserList);
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 1, browserSelect);

        JMeterPluginUtils.addToPanel(this, labelConstraints, 0, 2, new JLabel("Enable log: ", JLabel.RIGHT));
        JMeterPluginUtils.addToPanel(this, editConstraints, 1, 2, isEnableLog = new JCheckBox());

        JPanel container = new JPanel(new BorderLayout());
        container.add(this, BorderLayout.NORTH);
        return container;
    }


}
