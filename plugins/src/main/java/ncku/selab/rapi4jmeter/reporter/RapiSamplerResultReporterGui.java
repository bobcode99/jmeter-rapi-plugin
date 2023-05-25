package ncku.selab.rapi4jmeter.reporter;

import ncku.selab.rapi4jmeter.JMeterPluginUtils;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;

import java.awt.*;

public class RapiSamplerResultReporterGui extends AbstractListenerGui {

    protected final RapiSamplerResultCollector collector = new RapiSamplerResultCollector();

    public RapiSamplerResultReporterGui() {
        createGui();
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
//        add(startGenerate, BorderLayout.SOUTH);
        RapiResultPanel rapiResultPanel = new RapiResultPanel();
        add(rapiResultPanel, BorderLayout.CENTER);
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
        return JMeterPluginUtils.prefixLabel("Rapi Result Generator");
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

