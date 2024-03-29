package ncku.selab.rapi4jmeter;

import javax.swing.*;
import java.awt.*;

public class JMeterPluginUtils {

    public static String prefixLabel(String label) {
        String PLUGINS_PREFIX = "@rapi - ";
        return PLUGINS_PREFIX + label;
    }

    public static void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }

}

