package team.sideex.sampler;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
//import org.example.JMeterPluginUtils;

import java.awt.*;

public class SideexSamplerGui extends AbstractSamplerGui {

    private final SideexSamplerPanel sideexSamplerPanel;

    public SideexSamplerGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);

        this.sideexSamplerPanel = new SideexSamplerPanel();
        add(sideexSamplerPanel.init(), BorderLayout.CENTER);
        sideexSamplerPanel.initFields();
    }

    @Override
    public String getLabelResource() {
//        System.out.println("getLabelResource");
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        //set display name
//        System.out.println("getStaticLabel");
        return JMeterPluginUtils.prefixLabel("SideexSampler");
    }

    @Override
    public TestElement createTestElement() {
        // create new sampler (when create sampler or save sampler)
//        System.out.println("createTestElement");
        TestElement sampler = new SideexSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    @Override
    public void configure(TestElement element) {
        // to read user input

//        System.out.println("GUI configure");
        super.configure(element);
        sideexSamplerPanel.configure(element);
    }

    @Override
    public void modifyTestElement(TestElement element) {
        // save user input to Jmeter
//        System.out.println("GUI modifyTestElement");
        super.configureTestElement(element);
        sideexSamplerPanel.modifyTestElement(element);
    }

    @Override
    public void clearGui() {
//        System.out.println("clearGui");

        super.clearGui();
        sideexSamplerPanel.initFields();
    }

}
