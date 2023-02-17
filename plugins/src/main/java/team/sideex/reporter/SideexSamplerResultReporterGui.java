package team.sideex.reporter;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;
import team.sideex.JMeterPluginUtils;

import java.awt.*;

public class SideexSamplerResultReporterGui extends AbstractListenerGui {

    protected final SideexSamplerResultCollector collector = new SideexSamplerResultCollector();

    public SideexSamplerResultReporterGui() {
        createGui();
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
//        add(startGenerate, BorderLayout.SOUTH);
        SideexResultPanel sideexResultPanel = new SideexResultPanel();
        add(sideexResultPanel, BorderLayout.CENTER);
    }

    @Override
    public TestElement createTestElement() {
        modifyTestElement(collector);
        return collector;
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginUtils.prefixLabel("SideexResultGenerator");
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
    }

    @Override
    public void clearGui() {
        super.clearGui();
    }

}

