package org.example.sampler;

import org.apache.jmeter.samplers.SampleResult;

public class TestSamplerResult extends SampleResult {
    private String name = null;

    public TestSamplerResult() {
        sampleStart();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
