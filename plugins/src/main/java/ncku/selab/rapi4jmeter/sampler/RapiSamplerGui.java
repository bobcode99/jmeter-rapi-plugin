package ncku.selab.rapi4jmeter.sampler;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import ncku.selab.rapi4jmeter.JMeterPluginUtils;

import java.awt.*;

public class RapiSamplerGui extends AbstractSamplerGui {

    private RapiSamplerPanel rapiSamplerPanel;

    public RapiSamplerGui() {
        createGui();
    }

    private void createGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);

        this.rapiSamplerPanel = new RapiSamplerPanel();
        add(rapiSamplerPanel.init(), BorderLayout.CENTER);
        rapiSamplerPanel.initFields();
    }

    @Override
    public String getLabelResource() {
//        System.out.println("getLabelResource");
        return this.getClass().getSimpleName();
//        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        //set display name
//        System.out.println("getStaticLabel");
        return JMeterPluginUtils.prefixLabel("Rapi Sampler");
    }

    @Override
    public TestElement createTestElement() {
        // create new sampler (when create sampler or save sampler)
//        System.out.println("createTestElement");
        TestElement sampler = new RapiSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    @Override
    public void configure(TestElement element) {
        // to read user input

//        System.out.println("GUI configure");
        super.configure(element);
        rapiSamplerPanel.configure(element);
    }

    @Override
    public void modifyTestElement(TestElement element) {
        // save user input to Jmeter
//        System.out.println("GUI modifyTestElement");
        super.configureTestElement(element);
        rapiSamplerPanel.modifyTestElement(element);
    }

    @Override
    public void clearGui() {
//        System.out.println("clearGui");

        super.clearGui();
        rapiSamplerPanel.initFields();
    }

}
