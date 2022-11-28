package org.example.gui;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.example.TestSampler;

import java.awt.*;
import javax.swing.JPanel;
//import java.awt.BorderLayout;


public class TestSamplerGui extends AbstractSamplerGui{

    private final TestSamplerPanel testSamplerPanel;

    public TestSamplerGui(){
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);

        this.testSamplerPanel = new TestSamplerPanel();
        add(testSamplerPanel.init(), BorderLayout.CENTER);
        testSamplerPanel.initFields();
    }

    @Override
    public String getLabelResource() {
        System.out.println("getLabelResource");
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        //set display name
        System.out.println("getStaticLabel");

        return "TestSampler";
    }

    @Override
    public TestElement createTestElement() {//创建所对应的Sampler
        System.out.println("createTestElement");
        TestElement sampler = new TestSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    @Override
    public void configure(TestElement element) {
        System.out.println("GUI configure");
        super.configure(element);
        testSamplerPanel.configure(element);
    }

    @Override
    public void modifyTestElement(TestElement element) {
        System.out.println("GUI modifyTestElement");
        super.configureTestElement(element);
        testSamplerPanel.modifyTestElement(element);
    }

    @Override
    public void clearGui() {
        System.out.println("clearGui");

        super.clearGui();
        testSamplerPanel.initFields();
    }

}
