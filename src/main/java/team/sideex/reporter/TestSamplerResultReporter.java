package team.sideex.reporter;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;
import team.sideex.JMeterPluginUtils;

public class TestSamplerResultReporter extends AbstractListenerGui{

    @Override
    public TestElement createTestElement() {
        TestSamplerResultCollector collector = new TestSamplerResultCollector();
        modifyTestElement(collector);
        return collector;
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginUtils.prefixLabel("SamplerResultReport");
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);

    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
    }

}

