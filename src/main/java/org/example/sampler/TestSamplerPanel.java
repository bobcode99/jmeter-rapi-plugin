package org.example.sampler;

import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

public class TestSamplerPanel extends JPanel {
    private JCheckBox isSuccessful;

    private JTextField responseLabel;

    private JTextField responseCode;
    private JTextField responseMessage;
    private JTextField tcFilePath;


    public TestSamplerPanel() {
        super(new GridBagLayout());
    }

    public void configure(TestElement element) {
        // to read user input

        System.out.println("PANEL configure");

        if (element instanceof TestSampler) {
            TestSampler dummySampler = (TestSampler) element;
            responseLabel.setText(dummySampler.getLabel());
            responseCode.setText(dummySampler.getResponseCode());
            isSuccessful.setSelected(dummySampler.getSuccessful());
            responseMessage.setText(dummySampler.getResponseMessage());
            tcFilePath.setText(dummySampler.getTestCaseFilePath());
        }

    }

    public void modifyTestElement(TestElement element) {
        // save user input to Jmeter

        System.out.println("PANEL modifyTestElement");
        if (element instanceof TestSampler) {
            TestSampler dummySampler = (TestSampler) element;
            dummySampler.setLabel(responseLabel.getText());
            dummySampler.setResponseCode(responseCode.getText());
            dummySampler.setSuccessful(isSuccessful.isSelected());
            dummySampler.setResponseMessage(responseMessage.getText());

            dummySampler.setTestCaseFilePath(tcFilePath.getText());


        }

    }

    public void initFields() {
        isSuccessful.setSelected(true);
        responseCode.setText("200");
        responseMessage.setText("response this: 🌟 !!");
        tcFilePath.setText("");
    }


    public JPanel init() {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;


        addToPanel(this, labelConstraints, 0, 0, new JLabel("Successful sample: ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 0, isSuccessful = new JCheckBox());
        addToPanel(this, labelConstraints, 0, 1, new JLabel("response code: ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 1, responseCode = new JTextField());

//        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
//        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(this, labelConstraints, 0, 2, new JLabel("response message: ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 2, responseMessage = new JTextField());
        addToPanel(this, labelConstraints, 0, 3, new JLabel("response label: ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 3, responseLabel = new JTextField());
        addToPanel(this, labelConstraints, 0, 4, new JLabel("Test case file path: ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 4, tcFilePath = new JTextField());

        JPanel container = new JPanel(new BorderLayout());
        container.add(this, BorderLayout.NORTH);
        return container;
    }


    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }

}
