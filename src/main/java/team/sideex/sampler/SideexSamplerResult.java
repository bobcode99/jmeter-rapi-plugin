package team.sideex.sampler;

import org.apache.jmeter.samplers.SampleResult;

public class SideexSamplerResult extends SampleResult {
    private String name = null;

    public SideexSamplerResult() {
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
