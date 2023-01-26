package team.sideex.sampler;

import org.apache.jmeter.samplers.SampleResult;

public class TestSamplerResult extends SampleResult {
    private String name = null;

    public TestSamplerResult() {
        System.out.println("sample Start");
        sampleStart();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
