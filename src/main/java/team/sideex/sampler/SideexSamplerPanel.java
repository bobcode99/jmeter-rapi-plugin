package team.sideex.sampler;

import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SideexSamplerPanel extends JPanel {
    private JTextField tcFilePath;
    private JComboBox<String> browserSelect;

    public SideexSamplerPanel() {
        super(new GridBagLayout());
    }

    public void configure(TestElement element) {
        // to read user input

//        System.out.println("PANEL configure");

        if (element instanceof SideexSampler) {
            SideexSampler sideexSampler = (SideexSampler) element;
            tcFilePath.setText(sideexSampler.getTestCaseFilePath());
            browserSelect.setSelectedItem(sideexSampler.getBrowserSelect());
        }

    }

    public void modifyTestElement(TestElement element) {
        // save user input to Jmeter

//        System.out.println("PANEL modifyTestElement");
        if (element instanceof SideexSampler) {
            SideexSampler sideexSampler = (SideexSampler) element;
            sideexSampler.setTestCaseFilePath(tcFilePath.getText());
            sideexSampler.setBrowserSelect(Objects.requireNonNull(browserSelect.getSelectedItem()).toString());
        }

    }

    public void initFields() {
        tcFilePath.setText("path/to/testCase.json");
        browserSelect.setSelectedItem("Firefox");
    }


    public JPanel init() {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        addToPanel(this, labelConstraints, 0, 0, new JLabel("Test case file path: ", JLabel.RIGHT));
        addToPanel(this, editConstraints, 1, 0, tcFilePath = new JTextField());


        addToPanel(this, labelConstraints, 0, 1, new JLabel("Select browser to run test: ", JLabel.RIGHT));
        String[] browserList = {"Chrome", "Firefox", "MicrosoftEdge"};

        browserSelect = new JComboBox<>(browserList);
        addToPanel(this, editConstraints, 1, 1, browserSelect);

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
